# ---------- STAGE 1: BUILD ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia solo il pom prima (cache Docker migliore)
COPY pom.xml .

# Copia il codice
COPY src ./src

# Build del jar
RUN mvn clean install package -DskipTests


# ---------- STAGE 2: RUNTIME ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copia solo il jar finale
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]