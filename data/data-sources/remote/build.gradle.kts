plugins {
    id("base-library-build")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.remote"
}

dependencies {
    implementation(project(":common"))

    implementation(libs.jetbrains.kotlinx.kotlinxCoroutinesPlayServices)
    implementation(libs.google.android.gms.playServicesLocation)
    implementation(libs.refrofit2.retrofit)
    implementation(libs.refrofit2.converterMoshi)
    implementation(libs.moshi.moshiKotlin)
    ksp(libs.moshi.moshiKotlinCodegen)
    implementation(libs.okhttp3.loggingInterceptor)
    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltAndroidCompiler)

    testImplementation(project(":common:test-utils"))
}