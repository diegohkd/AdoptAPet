plugins {
    id("base-library-build")
}

android {
    namespace = "com.mobdao.adoptapet.dataapi"
}

dependencies {
    implementation(project(":domain:entities"))

    implementation(libs.jetbrains.kotlinx.kotlinxCoroutinesCore)
}
