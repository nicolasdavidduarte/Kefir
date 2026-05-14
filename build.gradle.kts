import com.diffplug.gradle.spotless.SpotlessExtension

// /build.gradle.kts (Root)
plugins {
    // These match the versions from your snippets
    kotlin("jvm") version "2.3.0" apply false
    kotlin("plugin.spring") version "2.3.0" apply false
    kotlin("plugin.jpa") version "2.3.0" apply false

    id("org.springframework.boot") version "3.5.14" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.diffplug.spotless") version "8.4.0" apply false
    id("com.github.ben-manes.versions") version "0.51.0" apply false
}

subprojects {
    // This is the modern way to apply a plugin by ID inside a subprojects block
    pluginManager.apply("com.diffplug.spotless")

    // We use the extension class directly to be safe
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension>("spotless") {
        kotlin {
            target("src/**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint("1.8.0")
        }
        java {
            target("src/**/*.java")
            targetExclude("**/build/**/*.java")
            googleJavaFormat("1.35.0")
            removeUnusedImports()
        }
    }
}