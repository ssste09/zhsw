#!/usr/bin/env bash
set -euo pipefail

# -------------------------------------------------------
# Paths (relative to repo root)
# -------------------------------------------------------
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)/zhsw"

TOOLS_DIR="$ROOT_DIR/tools"
GENERATOR_VERSION="7.7.0"
GENERATOR_JAR="$TOOLS_DIR/openapi-generator-cli-$GENERATOR_VERSION.jar"
OPENAPI=("java" "-jar" "$GENERATOR_JAR")

SPECS_ROOT="$ROOT_DIR/libs/specs"
AUTH_SPECS_DIR="$SPECS_ROOT/auth"               # backend spec folder for this service
BACKEND_AUTH_DIR="$ROOT_DIR/apps/backends/auth"

UI_MOBILE_PKG="$ROOT_DIR/apps/ui_mobile/package.json"

# Common TS generator options
TS_GEN_ADDITIONAL="npmVersion=0.0.1,typescriptThreePlus=true,withInterfaces=true,useSingleRequestParameter=true"

# -------------------------------------------------------
# 0. Ensure JAR exists
# -------------------------------------------------------
if [ ! -f "$GENERATOR_JAR" ]; then
  echo "⬇️  Fetching OpenAPI Generator CLI $GENERATOR_VERSION..."
  mkdir -p "$TOOLS_DIR"
  curl -sSL -o "$GENERATOR_JAR" \
    "https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$GENERATOR_VERSION/openapi-generator-cli-$GENERATOR_VERSION.jar"
fi

echo "✅ Using generator: $GENERATOR_JAR"

# -------------------------------------------------------
# 1. Generate Spring backend for auth
# -------------------------------------------------------
if [ -d "$AUTH_SPECS_DIR" ]; then
  echo "🚀 Generating Spring backend from $AUTH_SPECS_DIR ..."
  find "$AUTH_SPECS_DIR" -type f \( -name "*.yaml" -o -name "*.json" \) | while read -r spec; do
    relpath="${spec#$AUTH_SPECS_DIR/}"                 # path relative to auth specs dir
    outdir="$BACKEND_AUTH_DIR/target/$(dirname "$relpath")"
    mkdir -p "$outdir"
    echo "🔹 Spring backend <- $spec -> $outdir"
    "${OPENAPI[@]}" generate \
      -g spring \
      -i "$spec" \
      -o "$outdir" \
      --skip-validate-spec \
      --library spring-boot \
      --additional-properties=useJakartaEe=true
  done
else
  echo "⚠️  No auth specs dir found at $AUTH_SPECS_DIR (skipping backend generation)"
fi

# -------------------------------------------------------
# 2. Generate TS libs for EACH folder under libs/specs/*
# -------------------------------------------------------
echo "📱 Generating TypeScript clients for all specs in $SPECS_ROOT ..."

LIB_NAMES=()
LIB_DIRS=()

for specdir in "$SPECS_ROOT"/*; do
  [ -d "$specdir" ] || continue   # only directories
  name="$(basename "$specdir")"

  # find a spec file in this folder
  spec_file="$(find "$specdir" -maxdepth 1 -type f \( -name "*.yaml" -o -name "*.yml" -o -name "*.json" \) | head -n 1 || true)"
  if [ -z "$spec_file" ]; then
    echo "⚠️  No spec file in $specdir, skipping"
    continue
  fi

    pkg_name="@myorg/${name}_api"
  out_dir="$ROOT_DIR/libs/${name}_api"

  echo "📦 TS client $pkg_name from $spec_file -> $out_dir"

  # clean everything for a fresh lib
  rm -rf "$out_dir"
  mkdir -p "$out_dir"

  # 2.a Generate TS sources into out_dir (generator will create out_dir/src)
  "${OPENAPI[@]}" generate \
    -g typescript-fetch \
    -i "$spec_file" \
    -o "$out_dir" \
    --skip-validate-spec \
    --additional-properties="npmName=$pkg_name,$TS_GEN_ADDITIONAL"

  # 2.b tsconfig for src -> dist in this lib
  cat > "$out_dir/tsconfig.json" <<EOF
{
  "compilerOptions": {
    "target": "ES2019",
    "module": "ESNext",
    "moduleResolution": "Node",
    "rootDir": "src",
    "outDir": "dist",
    "strict": true,
    "declaration": true,
    "esModuleInterop": true,
    "skipLibCheck": true
  },
  "include": ["src/**/*.ts"]
}
EOF

  # 2.c package.json exporting ONLY dist (and using local tsc)
  cat > "$out_dir/package.json" <<EOF
{
  "name": "$pkg_name",
  "version": "0.0.1",
  "main": "src/index.ts",
  "types": "dist/index.d.ts",
  "scripts": {
    "build": "tsc -p tsconfig.json"
  },
  "devDependencies": {
    "typescript": "^5.6.0"
  }
}
EOF

  # 2.d build this lib using its own local TypeScript
  echo "🛠  Building $pkg_name ..."
  (cd "$out_dir" && npm install --ignore-scripts --no-fund --no-audit && npx tsc -p tsconfig.json)

  LIB_NAMES+=("$pkg_name")
  LIB_DIRS+=("$out_dir")

done

# -------------------------------------------------------
# 3. Wire them into apps/ui_mobile/package.json
# -------------------------------------------------------
if [ ! -f "$UI_MOBILE_PKG" ]; then
  echo "⚠️  ui_mobile package.json not found at $UI_MOBILE_PKG, skipping dependency wiring"
else
  if [ "${#LIB_NAMES[@]}" -eq 0 ]; then
    echo "ℹ️  No TS libs generated; ui_mobile package.json unchanged."
  else
    echo "🧩 Updating ui_mobile package.json with local API libs..."

    # Build a JSON array for Node to consume
    json="["
    for i in "${!LIB_NAMES[@]}"; do
      [ "$i" -gt 0 ] && json+=","
      name="${LIB_NAMES[$i]}"
      dir="${LIB_DIRS[$i]}"
      # escape double quotes in dir
      esc_dir="${dir//\"/\\\"}"
      json+="{\"name\":\"$name\",\"dir\":\"$esc_dir\"}"
    done
    json+="]"

    GENERATED_LIBS="$json" node <<'EOF'
const fs = require('fs');
const path = require('path');

const libs = JSON.parse(process.env.GENERATED_LIBS || '[]');
if (!libs.length) {
  console.log('No generated libs to add, skipping.');
  process.exit(0);
}

const pkgPath = path.resolve('apps/ui_mobile/package.json');
if (!fs.existsSync(pkgPath)) {
  console.error('Cannot find', pkgPath);
  process.exit(1);
}

const pkg = JSON.parse(fs.readFileSync(pkgPath, 'utf8'));
pkg.dependencies = pkg.dependencies || {};

for (const lib of libs) {
  const rel = path.relative(path.dirname(pkgPath), lib.dir).replace(/\\/g, '/');
  pkg.dependencies[lib.name] = `file:${rel}`;
}

fs.writeFileSync(pkgPath, JSON.stringify(pkg, null, 2));
console.log('✅ Updated', pkgPath, 'with', libs.length, 'local API libs.');
EOF

  fi
fi

echo "🎉 Done: backend + TS clients + ui_mobile deps wired."
