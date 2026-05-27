FROM eclipse-temurin:21-jdk-alpine

# Update and install curl, ping, telnet, then clean apt cache
RUN apk update && \
    apk add --no-cache iputils curl busybox-extras && \
    rm -rf /var/cache/apk/*

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
