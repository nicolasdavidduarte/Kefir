pluginManagement {
    plugins {
        kotlin("plugin.jpa") version "2.4.21"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "Kefir"
include("backend-web")
include("batch-jobs")
include("domain")
include("infrastructure")
