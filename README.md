# What is Kefir?
Kefir is a mini banking core, with a simplified approach in order to understand better things
that I already know and learn new ones. 

# Languages / Frameworks
This system is developed using Java 21 and Kotlin with Spring Boot.

## Dependencies
This project uses:
* Hibernate to connect to the database and perform operations
* Liquibase to execute database scripts
* Micrometer for metrics to be consumed with Prometheus, Datadog and Grafana
* Spring Security for authorization
* Spring Batch for massive processing
* Amazon SNS for message delivery

## Bundles
This project use Gradle for maintaining the bundles.

## Setup
Building this project requires using a PostgreSQL database with Kefir core tables (Environment image currently is WIP).
Initial development created on a MacBook Air M2 32 GB using Java 17 with IntelliJ IDEA CE