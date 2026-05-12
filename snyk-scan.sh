#!/bin/bash

set -e

# Colors
BLUE='\033[0;34m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

print_status() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 1. Get the directory where the script is located (The Root)
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# --- GUARD RAIL ---
# This ensures you aren't accidentally running it from your base home directory
if [[ "$PROJECT_ROOT" == "$HOME" ]] || [[ "$PROJECT_ROOT" == "/" ]]; then
    print_error "Security Catch: You are running this from $PROJECT_ROOT."
    echo "Please ensure this script is inside the 'Kefir' folder, not just loose in your Home directory."
    exit 1
fi

cd "$PROJECT_ROOT"
print_status "Running Snyk scan in: $PROJECT_ROOT"

# --- SCAN 1: Open Source Dependencies (SCA) ---
# Snyk tests your build.gradle/settings.gradle for vulnerable libraries
print_status "Scanning application dependencies at $PROJECT_ROOT..."
snyk test --severity-threshold=high --all-projects

# --- SCAN 2: Infrastructure as Code (IaC) ---
# Similar to 'trivy config', this checks Dockerfiles, K8s manifests, or Terraform
print_status "Scanning configuration files (IaC)..."
snyk iac test "$PROJECT_ROOT" --severity-threshold=high

# --- SCAN 3: Backend Docker Image ---
print_status "Building and scanning backend Docker image..."
cd devops/docker
docker build -f dockerfile -t kefir-backend:latest ../../backend-web

# Snyk container scan uses the Dockerfile to provide base-image remediation advice
snyk container test kefir-backend:latest --file=dockerfile --severity-threshold=high

# --- SCAN 4: PostgreSQL Custom Image ---
print_status "Building and scanning PostgreSQL custom image..."
cd devops/docker/postgres-custom
docker build -f dockerfile -t kefir-postgres:latest .

snyk container test kefir-postgres:latest --file=dockerfile --severity-threshold=high

# --- SCAN 5: Code Analysis (SAST) ---
# Scans your source code (Java/Kotlin/etc) for insecure coding patterns
print_status "Performing Snyk Code analysis (SAST)..."
cd "$PROJECT_ROOT"
snyk code test

print_status "Snyk security scan completed successfully!"
print_warning "Review the findings above. Use 'snyk monitor' to track in the Snyk Dashboard."

# Cleanup
cd devops/docker
docker rmi kefir-backend:latest kefir-postgres:latest 2>/dev/null || true
print_status "Cleaned up temporary Docker images"