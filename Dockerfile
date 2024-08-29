# Stage 1: Build the application using Maven
FROM maven:3.9.7-eclipse-temurin-21 AS build

# Copy the pom.xml and the application source code to the container root directory
COPY pom.xml /pom.xml
COPY src /src

# Build the application without running tests
RUN mvn -f /pom.xml clean package -DskipTests

# Stage 2: Run the application using Eclipse Temurin JDK
FROM eclipse-temurin:21-jdk

# Copy the JAR file from the build stage to the root directory
COPY --from=build /target/*.jar /app.jar

# Expose the port the application runs on
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
