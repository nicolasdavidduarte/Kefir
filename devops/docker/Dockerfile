FROM eclipse-temurin:21-jdk-alpine

RUN apk update && \
    apk add --no-cache iputils curl busybox-extras && \
    rm -rf /var/cache/apk/*

WORKDIR /app

COPY backend-web/build/libs/backend-web-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
