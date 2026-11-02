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

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))

    // Spring Batch 6
    implementation("org.springframework.boot:spring-boot-starter-batch-jdbc")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // PostgreSQL
    runtimeOnly("org.postgresql:postgresql:42.7.14")

    // Kotlin
    implementation(kotlin("stdlib"))

    // JAXB
    implementation("jakarta.xml.bind:jakarta.xml.bind-api")
    runtimeOnly("org.glassfish.jaxb:jaxb-runtime")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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