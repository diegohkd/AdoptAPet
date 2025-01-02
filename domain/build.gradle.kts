plugins {
    id("base-library-build")
    alias(libs.plugins.dagger.hilt.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.adoptapet.domain"
}

dependencies {
    implementation(project(":domain:data-api"))
    implementation(project(":domain:entities"))
    implementation(project(":common"))

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltAndroidCompiler)

    testImplementation(project(":common:test-utils"))
}
