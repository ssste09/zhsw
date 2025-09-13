#!/usr/bin/env bash
set -euo pipefail

SPECS_ROOT="../../libs/specs"
PACKAGES_DIR="packages"

GENERATOR_VERSION="7.7.0"
TOOLS_DIR="tools"
GENERATOR_JAR="${TOOLS_DIR}/openapi-generator-cli-${GENERATOR_VERSION}.jar"

CLIENT_GENERATOR="typescript-fetch"  # or: typescript-axios
CLIENT_PROPS="supportsES6=true,typescriptThreePlus=true,useSingleRequestParameter=false"

UI_TSCONFIG="tsconfig.json"
UI_PKGJSON="package.json"

# ---- Toggle: 0 = types-only (default), 1 = runtime SDK with JS output ----
RUNTIME_SDK="${RUNTIME_SDK:-0}"

echo "📂 Specs root: $SPECS_ROOT"
echo "📦 Packages dir: $PACKAGES_DIR"
echo "⚙️  Mode: $([ "$RUNTIME_SDK" = "1" ] && echo 'Runtime SDK (JS + .d.ts)' || echo 'Types-only (.d.ts)')"

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

# --- Generate each SDK ---
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

  # ---------- package.json ----------
  if [ "$RUNTIME_SDK" = "1" ]; then
    # Runtime SDK: export built JS
    cat > "$PKG_DIR/package.json" <<JSON
{
  "name": "@your-scope/${svc}-client",
  "version": "0.0.0",
  "private": true,
  "type": "module",

  "types": "dist/index.d.ts",
  "exports": { ".": { "types": "./dist/index.d.ts", "default": "./dist/index.js" } },

  "files": ["dist", "README.md", "LICENSE"],
  "sideEffects": false,

  "scripts": {
    "build": "tsc -p tsconfig.build.json",
    "clean": "rimraf dist",
    "prepack": "npm run build"
  },
  "devDependencies": { "typescript": "^5.4.0", "rimraf": "^6.0.0" }
}
JSON
  else
    # Types-only: expose only .d.ts, no runtime
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
  fi

  # ---------- tsconfig.json (root; keep project empty to avoid TS server loading src) ----------
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
  "files": [],
  "include": []
}
JSON

  # ---------- tsconfig.build.json ----------
  if [ "$RUNTIME_SDK" = "1" ]; then
    # Emit JS + d.ts
    cat > "$PKG_DIR/tsconfig.build.json" <<JSON
{
  "extends": "./tsconfig.json",
  "compilerOptions": {
    "declaration": true,
    "emitDeclarationOnly": false,
    "declarationMap": false,
    "sourceMap": false,
    "outDir": "dist",
    "module": "ESNext",
    "target": "ES2020",
    "moduleResolution": "Bundler",
    "lib": ["ES2020", "DOM"]
  },
  "include": ["src"]
}
JSON
  else
    # Emit .d.ts only
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
  fi

  # ---------- index.ts (re-exports) ----------
  if [ "$RUNTIME_SDK" = "1" ]; then
    # Re-export runtime too so consumers can import Configuration, etc.
    cat > "$SRC_DIR/index.ts" <<'TS'
export * from "./generated/client/apis";
export * from "./generated/client/models";
export * from "./generated/client/runtime";
TS
  else
    cat > "$SRC_DIR/index.ts" <<'TS'
export * from "./generated/client/apis";
export * from "./generated/client/models";
TS
  fi

  # runtime stub only for types-only mode
  if [ "$RUNTIME_SDK" != "1" ]; then
    cat > "$PKG_DIR/empty.js" <<'JS'
// This package is type-only. No runtime exports.
JS
  else
    rm -f "$PKG_DIR/empty.js" 2>/dev/null || true
  fi

  # README
  cat > "$PKG_DIR/README.md" <<MD
# ${svc}-client

${RUNTIME_SDK:+Runtime }SDK generated from OpenAPI.
- ${RUNTIME_SDK:+Ships JS runtime and }TypeScript declarations in \`dist/\`.
- Entry: \`@your-scope/${svc}-client\`.
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

# --- Build all clients ---
for svc in $SERVICES; do
  echo "🔧 Building @your-scope/${svc}-client"
  (cd "$PACKAGES_DIR/${svc}-client" && (npm ci --silent || npm install --silent) && npm run build)
done

# --- Patch UI tsconfig: prefer dist, exclude packages/*/src (works for both modes) ---
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

# --- Add each client as a dependency in ui/package.json (version 0.0.0) ---
python3 - "$UI_PKGJSON" <<'PY'
import json, os, sys, glob
fn = sys.argv[1]
if not os.path.exists(fn):
    raise SystemExit("ui/package.json not found")
with open(fn) as f: data = json.load(f)
deps = data.setdefault("dependencies", {})
ws = data.setdefault("workspaces", [])
if isinstance(ws, dict): ws = ws.get("packages", ws)
if "." not in ws: ws.append(".")
if "packages/*" not in ws: ws.append("packages/*")
data["workspaces"] = ws
for pkgjson in glob.glob("packages/*-client/package.json"):
    with open(pkgjson) as f: meta = json.load(f)
    deps[meta["name"]] = "0.0.0"
with open(fn,"w") as f: json.dump(data,f,indent=2)
print(f"🛠  Added client deps to {fn}")
PY

echo ""
echo "✅ Generation complete."
echo "Next:"
echo "  npm install"
echo "  # Import from the package name only:"
if [ "$RUNTIME_SDK" = "1" ]; then
  echo "  #   import { AuthControllerApi, Configuration, type LoginUserRequest } from '@your-scope/<service>-client';"
  echo "  #   const api = new AuthControllerApi(new Configuration({ basePath: process.env.NEXT_PUBLIC_AUTH_URL }));"
else
  echo "  #   import type { LoginUserRequest } from '@your-scope/<service>-client';"
  echo "  #   // call fetch/SWR using those types"
fi


echo ""
echo "✅ Generation complete."
echo "Next steps (run in ./ui):"
for svc in $SERVICES; do
  cd $PACKAGES_DIR/${svc}-client
  npm run build
  cd ../../
done

npm ci
