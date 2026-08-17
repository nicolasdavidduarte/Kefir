plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.diffplug.spotless")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")

    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
    implementation("com.fasterxml.jackson.core:jackson-annotations")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}