// Trivy Security Scanning Gradle Script
// This script provides Gradle tasks for running Trivy scans

plugins {
    base
}

// Task to run Trivy filesystem scan
tasks.register("trivyFsScan") {
    group = "Security"
    description = "Run Trivy filesystem security scan"
    
    doLast {
        project.exec {
            commandLine("trivy", "fs", "--config", ".trivy.yaml", "--exit-code", "1", "--severity", "HIGH,CRITICAL", ".")
            workingDir(project.rootDir)
        }
    }
}

// Task to run Trivy configuration scan
tasks.register("trivyConfigScan") {
    group = "Security"
    description = "Run Trivy configuration security scan"
    
    doLast {
        project.exec {
            commandLine("trivy", "config", "--exit-code", "1", "--severity", "HIGH,CRITICAL", ".")
            workingDir(project.rootDir)
        }
    }
}

// Task to run Trivy secret scan
tasks.register("trivySecretScan") {
    group = "Security"
    description = "Run Trivy secret scanning"
    
    doLast {
        project.exec {
            commandLine("trivy", "fs", "--config", ".trivy.yaml", "--scanners", "secret", "--exit-code", "1", ".")
            workingDir(project.rootDir)
        }
    }
}

// Task to run Trivy Docker image scan (requires image to be built first)
tasks.register("trivyImageScan") {
    group = "Security"
    description = "Run Trivy Docker image security scan"
    
    doLast {
        project.exec {
            commandLine("trivy", "image", "--exit-code", "1", "--severity", "HIGH,CRITICAL", "kefir-backend:latest")
            workingDir(project.rootDir)
        }
    }
}

// Combined security scan task
tasks.register("trivySecurityScan") {
    group = "Security"
    description = "Run all Trivy security scans"
    
    dependsOn(tasks.named("trivyFsScan"))
    dependsOn(tasks.named("trivyConfigScan"))
    dependsOn(tasks.named("trivySecretScan"))
    
    doLast {
        println("All Trivy security scans completed successfully!")
        println("Note: Run 'trivyImageScan' separately after building Docker images")
    }
}
