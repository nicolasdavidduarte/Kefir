plugins {
    kotlin("jvm") version "2.3.0" apply false
    kotlin("plugin.spring") version "2.3.0" apply false
    kotlin("plugin.jpa") version "2.3.0" apply false

    id("org.springframework.boot") version "3.5.14" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.diffplug.spotless") version "8.4.0" apply false
    id("com.github.ben-manes.versions") version "0.51.0" apply false
    id("org.jetbrains.dokka") version "2.2.0" apply false
}

subprojects {
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