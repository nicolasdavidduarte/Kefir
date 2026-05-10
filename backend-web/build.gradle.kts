import org.gradle.api.tasks.*

import org.springframework.boot.gradle.tasks.run.BootRun

//tasks.withType<BootRun> {
//    jvmArgs(
//        "-javaagent:/opt/datadog/dd-java-agent.jar",
//        "-Ddd.agent.host=127.0.0.1",
//        "-Ddd.service=kefir",
//        "-Ddd.env=dev",
//        "-Ddd.version=1.0",
//        "-Ddd.trace.otlp.enabled=false",
//        "-Ddd.debugger.enabled=false",
//        "-Ddd.dynamic.instrumentation.enabled=false",
//        "-Ddd.logs.injection=true"
//    )
//}

plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.diffplug.spotless")
    id("com.github.ben-manes.versions") version "0.51.0"
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21) // Change to 21 if using Java 21
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")

    // AWS
    implementation("software.amazon.awssdk:sns:2.34.0")

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

}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

spotless {
    java {
        googleJavaFormat("1.17.0")
        target("src/**/*.java")
    }
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
