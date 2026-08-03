FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY gradle.properties .
COPY settings.gradle.kts .
COPY build.gradle.kts .

COPY backend-web/build.gradle.kts backend-web/
COPY batch-jobs/build.gradle.kts batch-jobs/
COPY domain/build.gradle.kts domain/

COPY --chmod=755 gradlew .

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

COPY . .

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :backend-web:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && \
    adduser -S spring -G spring

COPY --chown=spring:spring \
    --from=build \
    /app/backend-web/build/libs/*.jar \
    app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]