plugins {
    id("base-library-build")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.local"
}

dependencies {
    implementation(project(":common"))

    implementation(libs.androidx.room.roomRuntime)
    ksp(libs.androidx.room.roomCompiler)
    implementation(libs.androidx.room.roomKtx)
    implementation(libs.google.dagger.hiltAndroid)
    ksp(libs.google.dagger.hiltAndroidCompiler)
    implementation(libs.moshi.moshiKotlin)
    ksp(libs.moshi.moshiKotlinCodegen)

    testImplementation(project(":common:test-utils"))
}