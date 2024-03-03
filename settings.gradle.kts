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
include(":data")
include(":data:services:cache")
include(":data:services:remote")
include(":di")
include(":domain")
include(":domain:domain-api")
include(":ui")
