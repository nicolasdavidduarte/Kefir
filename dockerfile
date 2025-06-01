FROM openjdk:17-jdk-slim

# Update and install curl, ping, telnet, then clean apt cache
RUN apt-get update && \
    apt-get install -y iputils-ping curl telnet && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
