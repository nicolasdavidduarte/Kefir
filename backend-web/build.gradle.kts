plugins {
    id("java")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.diffplug.spotless")
    id("com.github.ben-manes.versions")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

extra["junit-jupiter.version"] = "6.0.3"
extra["tomcat.version"] = "10.1.55"
dependencies {
    constraints {
        // Fixes High Severity Netty Smuggling & Resource Allocation
        val secureNettyVersion = "4.1.133.Final"
        implementation("io.netty:netty-codec:$secureNettyVersion")
        implementation("io.netty:netty-codec-http:$secureNettyVersion")
        implementation("io.netty:netty-codec-http2:$secureNettyVersion")
        implementation("io.netty:netty-handler:$secureNettyVersion")
        implementation("io.netty:netty-common:$secureNettyVersion")
        implementation("io.netty:netty-buffer:$secureNettyVersion")
        implementation("io.netty:netty-transport:$secureNettyVersion")
        implementation("io.netty:netty-resolver:$secureNettyVersion")
        // Fixes High Severity Uncontrolled Recursion in Commons Lang
        implementation("org.apache.commons:commons-lang3:3.18.0")
    }

    // Project Modules
    implementation(project(":domain"))

    // Spring Boot Starters (Versions managed by Parent/BOM)
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Database & Infrastructure
    runtimeOnly("org.postgresql:postgresql:42.7.11")
    implementation("org.liquibase:liquibase-core:5.0.2")

    // AWS
    implementation("software.amazon.awssdk:sns:2.44.4")

    // Monitoring
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    //implementation("io.opentelemetry:opentelemetry-exporter-otlp")

    // API Documentation (OpenAPI 3)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // Security & JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // XML Support
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")

    // Utilities
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation(kotlin("stdlib"))

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:1.14.9")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks.named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates").configure {
    rejectVersionIf {
        val unstableKeywords = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea")
        unstableKeywords.any { candidate.version.lowercase().contains(it) }
    }
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "io.netty") {
            useVersion("4.1.133.Final")
            because("fixes High Severity Netty Smuggling and DoS vulnerabilities")
        }
        if (requested.group == "org.apache.commons" && requested.name == "commons-lang3") {
            useVersion("3.18.0")
            because("fixes High Severity Uncontrolled Recursion")
        }
    }
}