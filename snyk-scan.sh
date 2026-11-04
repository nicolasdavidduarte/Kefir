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
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_success() { echo -e "${GREEN}[OK]${NC} $1"; }

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

# Track Snyk findings across stages (docker/build failures still abort via set -e)
SCAN_FAILURES=0

run_snyk() {
    local stage_name="$1"
    shift
    set +e
    "$@"
    local exit_code=$?
    set -e
    if [[ $exit_code -eq 0 ]]; then
        print_success "$stage_name passed"
    elif [[ $exit_code -eq 1 ]]; then
        print_warning "$stage_name reported findings (exit 1); continuing remaining stages..."
        SCAN_FAILURES=$((SCAN_FAILURES + 1))
    else
        print_error "$stage_name did not complete its scan (exit $exit_code); continuing remaining stages..."
        SCAN_ERRORS=$((SCAN_ERRORS + 1))
    fi
}

# --- SCAN 1: Open Source Dependencies (SCA) ---
# Snyk tests your build.gradle/settings.gradle for vulnerable libraries
print_status "Scanning application dependencies at $PROJECT_ROOT..."
run_snyk "SCA" snyk test --severity-threshold=high --all-projects --policy-path="$PROJECT_ROOT/.snyk"

# --- SCAN 2: Infrastructure as Code (IaC) ---
# Similar to 'trivy config', this checks Dockerfiles, K8s manifests, or Terraform
print_status "Scanning configuration files (IaC)..."
run_snyk "IaC" snyk iac test "$PROJECT_ROOT" --severity-threshold=high

# --- SCAN 3: Backend Docker Image ---
# Dockerfile COPY paths expect the repo root as build context
print_status "Building and scanning backend Docker image..."
docker build --pull --no-cache \
  -f "$PROJECT_ROOT/devops/docker/Dockerfile" \
  -t kefir-backend:latest \
  "$PROJECT_ROOT"

# Snyk container scan uses the Dockerfile to provide base-image remediation advice
run_snyk "Container (backend)" snyk container test \
  kefir-backend:latest \
  --file="$PROJECT_ROOT/devops/docker/Dockerfile" \
  --severity-threshold=high \
  --policy-path="$PROJECT_ROOT/.snyk"

# --- SCAN 4: PostgreSQL Custom Image ---
print_status "Building and scanning PostgreSQL custom image..."

docker build --pull --no-cache \
  -f "$PROJECT_ROOT/devops/docker/postgres-custom/Dockerfile" \
  -t kefir-postgres:latest \
  "$PROJECT_ROOT/devops/docker/postgres-custom"

run_snyk "Container (postgres)" snyk container test \
  kefir-postgres:latest \
  --file="$PROJECT_ROOT/devops/docker/postgres-custom/Dockerfile" \
  --severity-threshold=high \
  --policy-path="$PROJECT_ROOT/.snyk"

# --- SCAN 5: Code Analysis (SAST) ---
# Scans your source code (Java/Kotlin/etc) for insecure coding patterns
print_status "Performing Snyk Code analysis (SAST)..."
cd "$PROJECT_ROOT"
run_snyk "Snyk Code" snyk code test

# Cleanup
docker rmi kefir-backend:latest kefir-postgres:latest 2>/dev/null || true
print_status "Cleaned up temporary Docker images"

print_warning "Review the findings above. Use 'snyk monitor' to track in the Snyk Dashboard."

if [[ $SCAN_FAILURES -gt 0 ]]; then
    print_error "Snyk security scan finished with $SCAN_FAILURES stage(s) reporting findings."
fi
if [[ $SCAN_ERRORS -gt 0 ]]; then
    print_error "Snyk security scan finished with $SCAN_ERRORS stage(s) reporting findings."
fi
if [[ $SCAN_FAILURES -gt 0 || $SCAN_ERRORS -gt 0 ]]; then
    exit 1
fi

print_success "Snyk security scan completed successfully!"
