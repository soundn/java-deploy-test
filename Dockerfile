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

# Build the application and rename the jar for easier reference
RUN ./mvnw package -DskipTests && \
    find target -name "*.jar" -not -name "*sources.jar" -not -name "*javadoc.jar" -exec cp {} app.jar \; && \
    ls -la app.jar

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

# Create log directories (both absolute and relative paths)
RUN mkdir -p /app/logs logs && \
    chown -R appuser:appuser /app/logs logs && \
    chmod 755 /app/logs logs

# Make sure the JAR file is accessible to the appuser
RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8185

# Add Java system property to specify log file location explicitly
CMD ["java", "-Dlogging.file.path=/app/logs", "-jar", "/app/app.jar"]
