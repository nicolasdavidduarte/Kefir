plugins {
    kotlin("jvm") version "2.3.0" apply false
    kotlin("plugin.spring") version "2.3.0" apply false
    kotlin("plugin.jpa") version "2.3.0" apply false

    id("org.springframework.boot") version "3.5.15" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.diffplug.spotless") version "8.4.0" apply false
    id("com.github.ben-manes.versions") version "0.54.0" apply false
    id("org.jetbrains.dokka") version "2.2.0" apply false
}

allprojects {
    group = "com.kefir"
    version = "1.0-SNAPSHOT"
}

subprojects {
    extra["logback.version"] = "1.5.36"
    extra["jackson.version"] = "2.21.5"
    extra["log4j2.version"] = "2.25.5"
    extra["tomcat.version"] = "10.1.56"
    extra["micrometer-tracing.version"] = "1.4.3"
    extra["opentelemetry.version"] = "1.62.0"
    extra["junit-jupiter.version"] = "6.0.3"
    extra["netty.version"] = "4.1.136.Final"
    extra["commons-lang3.version"] = "3.18.0"

    pluginManager.apply("com.diffplug.spotless")
    pluginManager.apply("org.jetbrains.dokka")

    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension>("spotless") {
        kotlin {
            target("src/**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint("1.8.0").editorConfigOverride(mapOf(
                "ktlint_standard_no-unused-imports" to "enabled"))

        }
        java {
            target("src/**/*.java")
            targetExclude("**/build/**/*.java")
            googleJavaFormat("1.35.0").reflowLongStrings()
            removeUnusedImports()
        }
    }
}