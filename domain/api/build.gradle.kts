plugins {
    id("base-library-build")
}

android {
    namespace = "com.mobdao.domain_api"
}

dependencies {
    implementation(libs.jetbrains.kotlinx.kotlinxCoroutinesCore)
}