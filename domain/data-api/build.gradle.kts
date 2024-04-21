plugins {
    id("base-library-build")
}

android {
    namespace = "com.mobdao.dataapi"
}

dependencies {
    implementation(libs.jetbrains.kotlinx.kotlinxCoroutinesCore)
}