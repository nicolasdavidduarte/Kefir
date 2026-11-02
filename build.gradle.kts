plugins {
    kotlin("jvm") version "2.3.0" apply false
    kotlin("plugin.spring") version "2.3.0" apply false
    kotlin("plugin.jpa") version "2.3.0" apply false

    id("org.springframework.boot") version "4.1.2" apply false
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
    extra["jackson-bom.version"] = "3.1.8"
    extra["jackson-2-bom.version"] = "2.21.8"
    extra["logback.version"] = "1.5.36"
    // Upgraded from 2.21.5 to fix SNYK-JAVA-COMFASTERXMLJACKSONCORE-21761837 (Critical Deserialization),
    // SNYK-JAVA-COMFASTERXMLJACKSONCORE-20059683/19778370 (High), SNYK-JAVA-COMFASTERXMLJACKSONCORE-21193287 (High DoS)
    extra["log4j2.version"] = "2.25.5"
    // Upgraded from 10.1.56 to fix SNYK-JAVA-ORGAPACHETOMCATEMBED-21707475/21707477 (Critical),
    // SNYK-JAVA-ORGAPACHETOMCATEMBED-21716509 (Critical HTTP Request Smuggling),
    // SNYK-JAVA-ORGAPACHETOMCATEMBED-20302843 (Critical Incorrect Authorization),
    // SNYK-JAVA-ORGAPACHETOMCATEMBED-20552494 (Critical Auth Bypass), and multiple High severity issues
    extra["tomcat.version"] = "10.1.61"
    // Upgraded from 1.15.12 to fix SNYK-JAVA-IOMICROMETER-19233327 (High CRLF Injection)
    // and SNYK-JAVA-IOMICROMETER-19256585 (High Memory Leak)
    extra["micrometer.version"] = "1.16.7"
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