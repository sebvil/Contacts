plugins {
    alias(libs.plugins.jvmApp)
    alias(libs.plugins.ktor)
}

group = "com.sebastianvm.contacts"

version = "1.0.0"

application {
    mainClass = "com.sebastianvm.contacts.ApplicationKt"
}

dependencies {
    implementation(project(":routes"))
    implementation(libs.logback)
    implementation(libs.bundles.ktorServer)
    testImplementation(libs.bundles.ktorClient)
    testImplementation(libs.ktor.serverTestHost)
}
