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

repositories {
    mavenCentral()
}

dependencies {

    constraints {
        val nettyVersion = "4.1.136.Final"
        implementation("io.netty:netty-codec:$nettyVersion")
        implementation("io.netty:netty-codec-http:$nettyVersion")
        implementation("io.netty:netty-codec-http2:$nettyVersion")
        implementation("io.netty:netty-handler:$nettyVersion")
        implementation("io.netty:netty-common:$nettyVersion")
        implementation("io.netty:netty-buffer:$nettyVersion")
        implementation("io.netty:netty-transport:$nettyVersion")
        implementation("io.netty:netty-resolver:$nettyVersion")

        implementation("org.apache.logging.log4j:log4j-to-slf4j:2.25.5")

        implementation("tools.jackson.core:jackson-databind:3.1.5")

        // Fixes High Severity Uncontrolled Recursion in Commons Lang
        implementation("org.apache.commons:commons-lang3:3.18.0")
        // Fixes CVE-2024-25710 and CVE-2024-26308
        testImplementation("org.apache.commons:commons-compress:1.26.0") {
            because("Fixes Infinite loop (CVE-2024-25710) and Resource Allocation (CVE-2024-26308)")
        }
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

    // Database & Infrastructure
    runtimeOnly("org.postgresql:postgresql:42.7.12")
    implementation("org.liquibase:liquibase-core:5.0.2")

    // Monitoring
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")

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

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // Logger
    implementation("org.slf4j:slf4j-api")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:1.14.9")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation(kotlin("test"))

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
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