plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.local"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
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