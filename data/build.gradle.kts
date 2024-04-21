import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("base-library-build")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.data"

    defaultConfig {
        buildConfigField(
            type = "String",
            name = "PET_FINDER_CLIENT_ID",
            value = gradleLocalProperties(rootDir, providers).getProperty("PET_FINDER_CLIENT_ID")
        )
        buildConfigField(
            type = "String",
            name = "PET_FINDER_CLIENT_SECRET",
            value = gradleLocalProperties(
                rootDir,
                providers
            ).getProperty("PET_FINDER_CLIENT_SECRET")
        )
        buildConfigField(
            type = "String",
            name = "GEOAPIFY_API_KEY",
            value = gradleLocalProperties(rootDir, providers).getProperty("GEOAPIFY_API_KEY")
        )
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data:data-sources:local"))
    implementation(project(":data:data-sources:remote"))
    implementation(project(":domain:data-api"))

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltAndroidCompiler)

    testImplementation(project(":common:test-utils"))
}