# Variables
SPECS_DIR=../../../libs/specs
GENERATOR_JAVA=target
OPENAPI=openapi-generator

generate_specs:
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



# Run backend (example: Auth service with Maven/Gradle)
run_server: generate_specs
	@echo "🚀 Starting Auth server..."
	./mvnw spring-boot:run -DskipTests
