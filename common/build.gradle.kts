plugins {
    id("base-library-build")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.common"
}

dependencies {
    api(libs.jakewharton.timber.timber)
    implementation(libs.refrofit2.converterMoshi)
    implementation(libs.moshi.moshiKotlin)
    implementation(libs.moshi.moshiKotlinCodegen)
    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltAndroidCompiler)

    testImplementation(project(":common:test-utils"))
}