# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Cache dependencies
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Create data volume directory for SQLite
RUN mkdir -p /app/data && chmod 777 /app/data
COPY --from=build /app/target/travel-insurance-1.0.0.jar app.jar
EXPOSE 8081
VOLUME /app/data
ENTRYPOINT ["java", "-jar", "app.jar"]
