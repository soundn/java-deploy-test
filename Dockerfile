FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy maven wrapper and make it executable
COPY mvnw mvnw
RUN chmod +x mvnw
COPY .mvn/ .mvn/

# Copy pom.xml and download dependencies
COPY pom.xml pom.xml
RUN ./mvnw dependency:go-offline -DskipTests

# Copy source code
COPY ./src src/

# Build the application
RUN ./mvnw package -DskipTests

# Set up a non-privileged user
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

# Create logs directory and set appropriate permissions
RUN mkdir -p /logs && \
    chown -R appuser:appuser /logs && \
    chmod 755 /logs

USER appuser

EXPOSE 8185

CMD ["java", "-jar", "target/*.jar"]
