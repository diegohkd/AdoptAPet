pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AdoptAPet"
include(":common")
include(":common:test-utils")
include(":data")
include(":data:data-sources:local")
include(":data:data-sources:remote")
include(":di")
include(":domain")
include(":domain:data-api")
include(":ui")
