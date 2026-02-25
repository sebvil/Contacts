plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    application
}

group = "com.sebastianvm.watcher"

version = "1.0.0"

application {
    mainClass.set("com.sebastianvm.watcher.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.bundles.db)
    implementation(libs.argon.jvm)
    implementation(libs.bundles.ktor.server)
    testImplementation(libs.testBalloon)
    testImplementation(libs.ktor.clientContentNegotiation)
    testImplementation(libs.ktor.clientResources)
    testImplementation(libs.kotest.assertions.core)
}
