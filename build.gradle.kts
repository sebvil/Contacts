import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.metro) apply false
    alias(libs.plugins.androidApp) apply false
    alias(libs.plugins.desktopApp) apply false
    alias(libs.plugins.kmpComposeLibrary) apply false
    alias(libs.plugins.webApp) apply false
    alias(libs.plugins.jvmApp) apply false
    alias(libs.plugins.kmpLibrary) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.testBalloon) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        target("**/src/*/kotlin/**/*.kt")
        configure()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        configure()
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    checkForGradleUpdate = true
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
    rejectPreReleases = true
    filterDeclaredConfigurations =
        Spec<String> {
            it != "androidJacocoAnt" && !it.startsWith("_internal-unified-test-platform")
        }
}

private fun BaseKotlinExtension.configure() {
    ktfmt().apply {
        kotlinlangStyle()
    }
}

val buildLogicClean = gradle.includedBuild("build-logic").task(":clean")
val rootBuildDir = layout.buildDirectory

tasks.named<Delete>("clean") {
    dependsOn(buildLogicClean)
}
