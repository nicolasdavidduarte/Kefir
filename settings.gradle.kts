pluginManagement {
    plugins {
        kotlin("plugin.lombok") version "2.3.0"
        kotlin("kapt") version "2.3.0"
    }
}
rootProject.name = "Kefir"
include("backend-web")
include("batch-jobs")
include("domain")
