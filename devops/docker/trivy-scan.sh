#!/bin/bash

# Trivy Docker Image Scanning Script for Kefir Project
# This script scans Docker images for vulnerabilities

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Trivy is installed
if ! command -v trivy &> /dev/null; then
    print_error "Trivy is not installed. Please install it first:"
    echo "  brew install trivy  # macOS"
    echo "  Or visit: https://github.com/aquasecurity/trivy"
    exit 1
fi

# Get the directory where the script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Point to the Kefir root (two levels up from devops/docker/)
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

# Navigate to the project root so Trivy sees the files
cd "$PROJECT_ROOT"

print_status "Scanning repository filesystem at $PROJECT_ROOT..."

# Scan the current directory (.) where pom.xml/build.gradle live
trivy fs --config "./.trivy.yaml" \
         --exit-code 1 \
         --severity HIGH,CRITICAL \
         --scanners vuln,secret,config .

# Scan 2: Build and scan backend Docker image
print_status "Building backend Docker image..."
cd devops/docker
docker build -f dockerfile -t kefir-backend:latest ../../backend-web

print_status "Scanning backend Docker image..."
trivy image --exit-code 1 --severity HIGH,CRITICAL kefir-backend:latest

## Scan 3: Build and scan PostgreSQL custom image
print_status "Building PostgreSQL custom Docker image..."
cd postgres-custom
docker build -f dockerfile -t kefir-postgres:latest .

print_status "Scanning PostgreSQL custom Docker image..."
trivy image --exit-code 1 --severity HIGH,CRITICAL kefir-postgres:latest

# Scan 4: Configuration files scan
print_status "Scanning configuration files for security issues..."
cd "$PROJECT_ROOT"
trivy config --exit-code 1 --severity HIGH,CRITICAL "$PROJECT_ROOT"

# Scan 5: Secret scanning
print_status "Scanning for secrets in repository..."
trivy repo --config "$PROJECT_ROOT/.trivy.yaml" --scanners secret --exit-code 1 "$PROJECT_ROOT"

print_status "Trivy security scan completed successfully!"
print_warning "Review any findings above and address high/critical vulnerabilities."

# Cleanup
cd devops/docker
docker rmi kefir-backend:latest kefir-postgres:latest 2>/dev/null || true
print_status "Cleaned up temporary Docker images"
