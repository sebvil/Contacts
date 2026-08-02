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
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
}
