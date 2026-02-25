import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.serialization)
}

kotlin {
    android {
        namespace = "com.sebastianvm.watcher.shared"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions { jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.get())) }
    }

    jvm()

    js {
        outputModuleName = "shared"
        browser { testTask { enabled = false } }
        binaries.library()
        generateTypeScriptDefinitions()
        compilerOptions { target = "es2015" }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.ktor.resources)
            implementation(libs.testBalloon)
        }
    }
}

tasks.withType<AbstractTestTask>().configureEach { failOnNoDiscoveredTests = false }
