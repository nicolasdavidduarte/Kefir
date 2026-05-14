plugins {
    id("java")
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.diffplug.spotless")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "org.kefir"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    // Core Spring Batch + JPA
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter")

    // Kotlin
    implementation(kotlin("stdlib"))

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.25")
    implementation("ch.qos.logback:logback-core:1.5.25")

    // Hibernate
    implementation("org.hibernate.orm:hibernate-core:6.2.11.Final")
    implementation("org.hibernate:hibernate-validator:6.2.0.Final")

    // Data Base
    runtimeOnly("org.postgresql:postgresql:42.7.11")

    // JAXB
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:3.0.2")

    // Lombok
    implementation("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

spotless {
    java {
        googleJavaFormat("1.35.0")
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
