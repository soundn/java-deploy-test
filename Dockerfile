# syntax=docker/dockerfile:1

# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:21-jdk-jammy as deps

WORKDIR /build

# Copy the mvnw wrapper and make it executable
COPY mvnw mvnw
RUN chmod +x mvnw
COPY .mvn/ .mvn/

# Copy pom.xml and download dependencies
COPY pom.xml pom.xml
RUN ./mvnw dependency:go-offline -DskipTests

################################################################################

# Create a stage for building the application based on the stage with downloaded dependencies.
FROM deps as package

WORKDIR /build

# Copy source code
COPY ./src src/
# Build the application
RUN ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Create a stage for extracting the application into separate layers.
FROM package as extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Create a new stage for running the application that contains the minimal
# runtime dependencies for the application.
FROM eclipse-temurin:21-jre-jammy AS final

# Create a non-privileged user that the app will run under.
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

# Copy the executable from the "extract" stage.
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

EXPOSE 8185

ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]
