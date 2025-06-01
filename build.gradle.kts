import org.gradle.api.tasks.*

plugins {
    id("java")
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.2"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Change to 21 if using Java 21
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.hibernate.orm:hibernate-core:6.2.11.Final") // Upgrade to Hibernate 6
    implementation("org.hibernate:hibernate-validator:6.2.0.Final")
    //implementation("org.slf4j:slf4j-api:1.7.30")
    //implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("ch.qos.logback:logback-core:1.4.11")
    implementation("org.postgresql:postgresql:42.7.2")  // Make sure to use the latest version
    implementation("com.mchange:c3p0:0.10.2")  // Or the latest version
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")
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
