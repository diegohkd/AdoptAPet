plugins {
    id("base-library-build")
}

android {
    namespace = "com.mobdao.common.testutils"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":data:data-sources:local"))
    implementation(project(":domain"))
    implementation(project(":domain:api"))
    api(libs.bundles.unitTest)
}