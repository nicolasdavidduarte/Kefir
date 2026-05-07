// /build.gradle.kts (Root)
plugins {
    // These match the versions from your snippets
    kotlin("jvm") version "2.0.21" apply false
    kotlin("plugin.spring") version "2.0.21" apply false
    kotlin("plugin.jpa") version "2.0.21" apply false

    id("org.springframework.boot") version "3.2.5" apply false

    // I noticed domain had 1.1.4 and backend-web had 1.1.2. 
    // We'll standardize on 1.1.4 here.
    id("io.spring.dependency-management") version "1.1.4" apply false

    // From your backend-web file
    id("com.diffplug.spotless") version "6.25.0" apply false
}