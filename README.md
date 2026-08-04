# What is Kefir?
Kefir is a simplified banking core system built to deepen my understanding of software engineering concepts and explore 
new technologies. It serves as a learning platform where I can experiment with architecture, security, cloud 
infrastructure, batch processing, and modern development practices.

# Technology Stack
* Java 21
* Kotlin 
* Spring Boot
* PostgreSQL 
* Gradle 
* Docker

## Live Demo
https://kefir.dedyn.io _(deployed in Google Cloud Platform)_

## Main Components
* Hibernate (JPA) for data persistence 
* Liquibase for database schema versioning and migrations 
* Micrometer for application metrics and observability 
* Spring Security for authentication and authorization 
* Spring Batch for batch and bulk processing

## Build Tool
This project uses Gradle for dependency management and build automation.


## Security Scanning
Kefir includes automated security scanning using Trivy and Snyk.
The scans check for:
* Dependency vulnerabilities 
* Container image vulnerabilities 
* Security misconfigurations 
* Exposed secrets and sensitive data

#### Quick Security Scan
### Trivy
```bash
./trivy-scan.sh
```

### SNYK
```bash
./snyk-scan.sh
```

## Requirements
* Java 21 
* PostgreSQL 
* Gradle 
* Docker (optional)


## Environment Variables
Before running the application, configure the following environment variable:

* Generate a secure value for JWT_SECRET env variable:

```bash
export JWT_SECRET=$(openssl rand -base64 32)
```

### Running with Docker:

1. Copy the example secrets:
```bash
cp devops/docker/secrets/postgres_password.txt.example devops/docker/secrets/postgres_password.txt
cp devops/docker/secrets/spring.datasource.password.example devops/docker/secrets/spring.datasource.password
cp devops/docker/secrets/jwt.secret.example devops/docker/secrets/jwt.secret
```

2. Generate a secure PostgreSQL password:
```bash
openssl rand -base64 32 > devops/docker/secrets/postgres_password.txt
```
3. Copy it so Spring Boot uses the same password:

```bash
cp devops/docker/secrets/postgres_password.txt devops/docker/secrets/spring.datasource.password
```

4. Generate a value for JWT_SECRET:
```bash
openssl rand -base64 32 > devops/docker/secrets/jwt.secret
```

* Start the environment:
```bash
docker compose up -d
```

# Features
* Customer management 
* Account management
* Loan creation 
* Loan amortization schedules 
* JWT authentication 
* Batch report generation 
* Metrics and observability


## Architecture
Kefir follows a layered architecture built with Spring Boot.

```text
Client
  │
  ▼
REST API (Controllers)
  │
  ▼
Business Logic (Services)
  │
  ▼
Persistence Layer (Repositories)
  │
  ▼
PostgreSQL
```


## API Documentation

Kefir provides interactive API documentation through Swagger UI.

After starting the application, open:

http://localhost:8080/swagger-ui/index.html


### Authentication

Most endpoints require JWT authentication.

1. Execute `POST /api/auth/login`
2. Copy the `accessToken` from the response
3. Click the **Authorize** button in Swagger UI
4. Paste the token and confirm
5. Execute any protected endpoint

Swagger will automatically include the following header in all requests:

Authorization: Bearer <your-token>

### OpenAPI Specification

The OpenAPI definition is available at:

http://localhost:8080/v3/api-docs