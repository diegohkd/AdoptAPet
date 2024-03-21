import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

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
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data:data-sources:local"))
    implementation(project(":data:data-sources:remote"))
    implementation(project(":domain:api"))

    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltAndroidCompiler)
}