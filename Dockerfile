# Étape 1 : builder
FROM maven:3.9.11-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests


# Étape 2 :  Image finale
FROM eclipse-temurin:17
WORKDIR /app
COPY --from=builder /app/target/*.jar api-social-network.jar
EXPOSE 8081
CMD ["java", "-jar", "api-social-network.jar"]
