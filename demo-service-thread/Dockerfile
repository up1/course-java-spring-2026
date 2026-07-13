FROM maven:3.9.16-eclipse-temurin-25-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests 

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/demo.jar ./demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]

