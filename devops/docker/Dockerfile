FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew :backend-web:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

RUN apk update && \
    apk add --no-cache iputils curl busybox-extras && \
    rm -rf /var/cache/apk/*

WORKDIR /app

COPY --from=builder /app/backend-web/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]