import BuildConstants.COMPILE_SDK
import BuildConstants.MIN_SDK
import BuildConstants.TARGET_SDK
import BuildConstants.VERSION_CODE
import BuildConstants.VERSION_NAME

plugins {
    id(
        libs.plugins.android.application
            .get()
            .pluginId,
    )
    id(
        libs.plugins.jetbrains.kotlin.android
            .get()
            .pluginId,
    )
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
}

android {
    namespace = "com.mobdao.adoptapet.presentation"
    compileSdk = COMPILE_SDK

    defaultConfig {
        applicationId = "com.mobdao.adoptapet"
        minSdk = MIN_SDK
        targetSdk = TARGET_SDK
        versionCode = VERSION_CODE
        versionName = VERSION_NAME

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":di-core"))
    implementation(project(":domain"))

    implementation(libs.androidx.paging.pagingCompose)
    implementation(libs.androidx.constraintlayout.constraintlayoutCompose)
    implementation(libs.google.accompanist.accompanistPermissions)
    implementation(libs.google.android.gms.playServicesLocation)
    implementation(libs.androidx.core.coreKtx)
    implementation(libs.androidx.lifecycle.lifecycleRuntimeKtx)
    implementation(libs.androidx.activity.activityCompose)
    implementation(platform(libs.androidx.compose.composeBom))
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.uiGraphics)
    implementation(libs.androidx.compose.ui.uiToolingPreview)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.androidx.navigation.navigationCompose)
    implementation(libs.androidx.lifecycle.lifecycleRuntimeCompose)
    implementation(libs.coilKt.coilCompose)
    implementation(libs.androidx.hilt.hiltNavigationCompose)
    implementation(libs.google.dagger.hiltAndroid)
    kapt(libs.google.dagger.hiltAndroidCompiler)
    implementation(libs.airbnb.android.lottieCompose)

    debugImplementation(libs.androidx.compose.ui.uiTooling)
    debugImplementation(libs.androidx.compose.ui.uiTestManifest)

    testImplementation(project(":common:test-utils"))
}
