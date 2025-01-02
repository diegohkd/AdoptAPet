plugins {
    id("base-library-build")
}

android {
    namespace = "com.mobdao.adoptapet.dicore"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))
}
