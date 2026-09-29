# ==========================================
# STAGE 1: Build the JAR with Maven
# ==========================================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and dependencies configuration first (Docker caching layer)
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Download dependencies offline to speed up subsequent builds
RUN ./mvnw dependency:go-offline -B

# Copy project source code and build the final executable JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ==========================================
# STAGE 2: Lightweight, Secure Production Runtime
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Security best practice: Run as a non-privileged user instead of root
RUN addgroup -S erpgroup && adduser -S erpuser -G erpgroup
USER erpuser

# Copy only the compiled JAR from Stage 1
COPY --from=builder /app/target/*.jar app.jar

# Render and cloud providers pass PORT via environment variables
ENV PORT=8080
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]