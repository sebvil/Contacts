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
    alias(libs.plugins.kotlin.plugin.parcelize) apply false
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

    outputFormatter {
        val res = buildString {
            appendLine("The following dependencies are up to date:")
            current.dependencies.forEach {
                appendLine("- ${it.group}:${it.name}:${it.version}")
            }
            appendLine()
            appendLine("The following dependencies can be updated:")
            outdated.dependencies
                .filter {
                    val fullName = "${it.group}:${it.name}"
                    "com.android." !in fullName &&
                        "org.jacoco" !in fullName &&
                        "hot-reload" !in fullName &&
                        "kotlin-build-tools" !in fullName
                }
                .forEach {
                    appendLine(
                        "- ${it.group}:${it.name}:${it.version} -> ${it.available.release ?: it.available.milestone ?: it.available.integration}"
                    )
                }
        }

        File(outputDir, "$reportfileName.txt").writeText(res)
    }

    rejectVersionIf {
        val isRejectableAlpha =
            "alpha" !in currentVersion.lowercase() && "alpha" in candidate.version.lowercase()
        val isRejectableBeta =
            "beta" !in currentVersion.lowercase() &&
                "beta" in candidate.version.lowercase() &&
                "androidx" !in candidate.group.lowercase()
        isRejectableAlpha || isRejectableBeta
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
