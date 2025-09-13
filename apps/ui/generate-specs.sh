#!/usr/bin/env bash
set -euo pipefail

SPECS_ROOT="../../libs/specs"
PACKAGES_DIR="packages"

GENERATOR_VERSION="7.7.0"
TOOLS_DIR="tools"
GENERATOR_JAR="${TOOLS_DIR}/openapi-generator-cli-${GENERATOR_VERSION}.jar"

CLIENT_GENERATOR="typescript-fetch"  # or: typescript-axios
CLIENT_PROPS="supportsES6=true,typescriptThreePlus=true,useSingleRequestParameter=false"
UI_PKGJSON="package.json"


UI_TSCONFIG="tsconfig.json"

echo "📂 Specs root: $SPECS_ROOT"
echo "📦 Packages dir: $PACKAGES_DIR"

# --- Ensure generator jar ---
if [ ! -f "$GENERATOR_JAR" ]; then
  echo "⬇️  Downloading OpenAPI Generator CLI ${GENERATOR_VERSION}"
  mkdir -p "$TOOLS_DIR"
  curl -sSL -o "$GENERATOR_JAR" \
    "https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/${GENERATOR_VERSION}/openapi-generator-cli-${GENERATOR_VERSION}.jar"
fi

# --- Detect services ---
SERVICES=""
for dir in "$SPECS_ROOT"/*; do
  if [ -d "$dir" ] && find "$dir" -type f \( -name "*.yaml" -o -name "*.yml" -o -name "*.json" \) | grep -q .; then
    SERVICES="$SERVICES $(basename "$dir")"
  fi
done
SERVICES="$(echo "$SERVICES" | xargs || true)"

if [ -z "${SERVICES:-}" ]; then
  echo "⚠️  No services found under $SPECS_ROOT"
  exit 1
fi
echo "🔎 Services: $SERVICES"

# --- Generate each SDK (types-only) ---
for svc in $SERVICES; do
  echo ""
  echo "==> Service: $svc"
  SPEC_FILE="$(find "$SPECS_ROOT/$svc" -type f \( -name "*.yaml" -o -name "*.yml" -o -name "*.json" \) | head -n1 || true)"
  if [ -z "$SPEC_FILE" ]; then
    echo "   ⚠️  No spec file for $svc"
    continue
  fi

  PKG_DIR="$PACKAGES_DIR/${svc}-client"
  SRC_DIR="$PKG_DIR/src"
  OUT_DIR="$SRC_DIR/generated/client"

  mkdir -p "$SRC_DIR"

  # package.json — types-only export
  cat > "$PKG_DIR/package.json" <<JSON
{
  "name": "@your-scope/${svc}-client",
  "version": "0.0.0",
  "private": true,
  "type": "module",

  "types": "dist/index.d.ts",
  "typesVersions": { "*": { "*": ["dist/*"] } },
  "exports": { ".": { "types": "./dist/index.d.ts", "default": "./empty.js" } },

  "files": ["dist", "empty.js", "README.md", "LICENSE"],
  "sideEffects": false,

  "scripts": {
    "build": "tsc -p tsconfig.build.json",
    "clean": "rimraf dist",
    "prepack": "npm run build"
  },
  "devDependencies": { "typescript": "^5.4.0", "rimraf": "^6.0.0" }
}
JSON

  # tsconfig.json — modern resolver, **no files** so TS server won't load src
  cat > "$PKG_DIR/tsconfig.json" <<JSON
{
  "compilerOptions": {
    "strict": true,
    "esModuleInterop": true,
    "resolveJsonModule": true,
    "skipLibCheck": true,
    "target": "ES2020",
    "module": "ESNext",
    "moduleResolution": "Bundler",
    "lib": ["ES2020", "DOM"]
  },
}
JSON

  # build emits declarations ONLY — **no declarationMap/sourceMap** (prevents jumps to src)
  cat > "$PKG_DIR/tsconfig.build.json" <<JSON
{
  "extends": "./tsconfig.json",
  "compilerOptions": {
    "declaration": true,
    "emitDeclarationOnly": true,
    "declarationMap": false,
    "sourceMap": false,
    "outDir": "dist"
  },
  "include": ["src"]
}
JSON

  # index.ts (type re-exports → dist/index.d.ts)
  cat > "$SRC_DIR/index.ts" <<'TS'
export * from "./generated/client/apis";
export * from "./generated/client/models";
TS

  # runtime stub
  cat > "$PKG_DIR/empty.js" <<'JS'
// This package is type-only. No runtime exports.
JS

  # readme
  cat > "$PKG_DIR/README.md" <<MD
# ${svc}-client

Type-only SDK generated from OpenAPI. Exposes \`.d.ts\` only (no runtime).
MD

  echo "   🧹 Cleaning old generated client"
  rm -rf "$OUT_DIR"
  mkdir -p "$OUT_DIR"

  echo "   🛠  Generating client from $SPEC_FILE → $OUT_DIR"
  java -jar "$GENERATOR_JAR" generate \
    -i "$SPEC_FILE" \
    -g "$CLIENT_GENERATOR" \
    -o "$OUT_DIR" \
    --skip-validate-spec \
    --additional-properties "$CLIENT_PROPS"
done

# --- Patch UI tsconfig: prefer dist, exclude packages/*/src ---
python3 - "$UI_TSCONFIG" <<'PY'
import json, os, sys, glob
fn = sys.argv[1]
data = {}
if os.path.exists(fn):
    with open(fn) as f: data = json.load(f)
co = data.setdefault("compilerOptions", {})
co.setdefault("target","ES2022")
co.setdefault("module","ESNext")
co.setdefault("moduleResolution","Bundler")
co.setdefault("jsx","preserve")
co.setdefault("strict", True)
co.setdefault("baseUrl",".")

paths = co.setdefault("paths", {})
for pkgjson in glob.glob("packages/*-client/package.json"):
    meta = json.load(open(pkgjson))
    name = meta["name"]
    pkgdir = os.path.dirname(pkgjson)
    paths[name] = [f"{pkgdir}/dist"]

data["include"] = ["src/**/*.ts","src/**/*.tsx","next-env.d.ts"]
ex = set(data.get("exclude", []))
ex.update({"node_modules","packages/*/src","packages/*/generated"})
data["exclude"] = sorted(ex)

with open(fn,"w") as f: json.dump(data,f,indent=2)
print(f"🛠  Patched {fn}: include src/*, exclude packages/*/src, paths→dist")
PY

# --- Add each client as a dependency in ui/package.json ---
python3 - "$UI_PKGJSON" <<'PY'
import json, os, sys, glob
fn = sys.argv[1]
if not os.path.exists(fn):
    raise SystemExit("ui/package.json not found")
with open(fn) as f: data = json.load(f)
deps = data.setdefault("dependencies", {})
# ensure workspaces field exists and includes packages/*
ws = data.setdefault("workspaces", [])
if isinstance(ws, dict): ws = ws.get("packages", ws)
if "." not in ws: ws.append(".")
if "packages/*" not in ws: ws.append("packages/*")
data["workspaces"] = ws
# add each service client at version 0.0.0
for pkgjson in glob.glob("packages/*-client/package.json"):
    with open(pkgjson) as f: meta = json.load(f)
    name = meta["name"]
    deps[name] = "0.0.0"
with open(fn,"w") as f: json.dump(data,f,indent=2)
print(f"🛠  Added client deps to {fn}")
PY

echo ""
echo "✅ Generation complete."
echo "Next steps (run in ./ui):"
for svc in $SERVICES; do
  cd $PACKAGES_DIR/${svc}-client
  npm run build
  cd ../../
done

npm ci
