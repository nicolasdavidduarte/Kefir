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

## Security Scanning with Trivy
This project includes comprehensive security scanning using Trivy. Trivy scans for:
- **Vulnerabilities** in dependencies and container images
- **Security misconfigurations** in configuration files
- **Secrets** and sensitive data in the codebase
- **Container image** security issues

### Running Security Scans

#### Quick Security Scan
```bash
# Run all security scans locally
./devops/docker/trivy-scan.sh
```

#### Individual Scans
```bash
# Filesystem vulnerability scan
trivy fs --config .trivy.yaml --severity HIGH,CRITICAL .

# Configuration security scan
trivy config --severity HIGH,CRITICAL .

# Secret scanning
trivy repo --config .trivy.yaml --scanners secret .

# Docker image scan (after building images)
trivy image --severity HIGH,CRITICAL kefir-backend:latest
```

#### Gradle Integration
```bash
# Run security scans via Gradle
./gradlew trivySecurityScan
./gradlew trivyFsScan
./gradlew trivyConfigScan
./gradlew trivySecretScan
./gradlew trivyImageScan  # Requires Docker images to be built
```

#### Dependency Updates Check
```bash
# Check for outdated dependencies (security-focused)
./gradlew dependencyUpdates
```

### Automated Scanning
- **GitHub Actions**: Automatic security scans run on:
  - Push to main/master/develop branches
  - Pull requests to main/master/develop branches
  - Weekly schedule (Sundays at 2 AM UTC)
- **Results**: Scan results are uploaded to GitHub Security tab as SARIF reports

### Security Configuration Files
- `.trivy.yaml`: Main Trivy configuration
- `.trivyignore`: Files/directories to ignore during scanning
- `.github/workflows/trivy-scan.yml`: Automated CI/CD scanning

### Installation
Install Trivy locally:
```bash
# macOS
brew install trivy

# Or visit: https://github.com/aquasecurity/trivy
```

## Setup
Building this project requires using a PostgreSQL database with Kefir core tables (Environment image currently is WIP).
Initial development created on a MacBook Air M2 32 GB using Java 17 with IntelliJ IDEA CE