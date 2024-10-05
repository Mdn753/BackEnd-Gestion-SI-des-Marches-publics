#### Stage 1: Build the application
FROM eclipse-temurin:22-jdk-alpine as build

# Set the current working directory inside the image
WORKDIR /app

# Copy the Maven wrapper and the necessary files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Build all the dependencies in preparation to go offline.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests

#### Stage 2: A minimal docker image with command to run the app
FROM eclipse-temurin:22-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/DPETL-0.0.1-SNAPSHOT.jar /app/DPETL.jar

# Expose the port that your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/DPETL.jar"]
