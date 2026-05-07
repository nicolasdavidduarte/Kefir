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
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.2"
    id("com.diffplug.spotless") version "6.25.0"
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    kotlin("plugin.jpa") version "2.0.21"
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
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")
    implementation("software.amazon.awssdk:sns:2.25.25")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    //implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.liquibase:liquibase-core")
    implementation(kotlin("stdlib-jdk8"))
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


