// /build.gradle.kts (Root)
plugins {
    // These match the versions from your snippets
    kotlin("jvm") version "2.2.0" apply false
    kotlin("plugin.spring") version "2.2.0" apply false
    kotlin("plugin.jpa") version "2.2.0" apply false

    id("org.springframework.boot") version "3.5.14" apply false

    id("io.spring.dependency-management") version "1.1.7" apply false

    id("com.diffplug.spotless") version "6.25.0" apply false

    id("com.github.ben-manes.versions") version "0.51.0" apply false
}