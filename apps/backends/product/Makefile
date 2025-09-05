# Paths / versions
TOOLS_DIR := tools
GENERATOR_VERSION := 7.7.0
GENERATOR_JAR := $(TOOLS_DIR)/openapi-generator-cli-$(GENERATOR_VERSION).jar

SPECS_DIR=../../../libs/specs/product
GENERATOR_JAVA=target

# Prefer java -jar; fall back to binary if present
OPENAPI ?= java -jar $(GENERATOR_JAR)

.PHONY: generate_specs run_tests run_tests_int run_tests_e2e run_server

# Ensure the JAR exists (download once)
$(GENERATOR_JAR):
	@mkdir -p $(TOOLS_DIR)
	@echo "⬇️  Fetching OpenAPI Generator CLI $(GENERATOR_VERSION)..."
	@curl -sSL -o $(GENERATOR_JAR) \
		https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$(GENERATOR_VERSION)/openapi-generator-cli-$(GENERATOR_VERSION).jar

generate_specs: $(GENERATOR_JAR)
	@echo "🚀 Generating OpenAPI specs recursively from $(SPECS_DIR)..."
	@find $(SPECS_DIR) -type f \( -name "*.yaml" -o -name "*.json" \) | \
	while read spec; do \
		relpath=$$(python3 -c "import os.path; print(os.path.relpath('$$spec', '$(SPECS_DIR)'))"); \
		output_java=$(GENERATOR_JAVA)/$$(dirname $$relpath); \
		mkdir -p $$output_java; \
		echo "🔹 Generating backend code for $$spec"; \
		$(OPENAPI) generate \
			-i $$spec \
			-g spring \
			-o $$output_java \
			--skip-validate-spec \
			--library spring-boot \
			--additional-properties useJakartaEe=true; \
	done

run_tests: generate_specs
	@echo "Starting product tests"
	./mvnw test -Dgroups=unit
run_tests_int: generate_specs
	@echo "Starting product tests"
	./mvnw verify -Dgroups=integration
run_tests_e2e: generate_specs
	@echo "Starting product tests"
	./mvnw verify -Dgroups=e2e

run_server: generate_specs
	@echo "🚀 Starting Auth server..."
	./mvnw spring-boot:run -DskipTests
