plugins {
    id("base-library-build")
}

android {
    namespace = "com.mobdao.di"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))
}