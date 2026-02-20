# Where the monorepo root is, relative to apps/ui_mobile
ROOT_DIR := $(abspath ../..)        # /Users/you/zhsw
GEN_SCRIPT := ./generate_specs.sh

# Change this to `yarn` or `pnpm` if you use those
NPM := npm

.PHONY: generate_specs watch_frontend

# Run the shared generator + install / update ui_mobile deps
generate_specs:
	@echo "🔁 Running OpenAPI generator script from $(GEN_SCRIPT)..."
	@cd $(ROOT_DIR) && $(GEN_SCRIPT)
	@echo "📦 Installing / updating local API dependencies for ui_mobile..."
	@cd $(ROOT_DIR)/apps/ui_mobile
	@npm install
	@echo "✅ ui_mobile is ready with fresh generated API libraries."

watch_frontend: generate_specs
	@npm start


