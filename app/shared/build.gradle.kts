import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi

plugins {
    alias(libs.plugins.kmpComposeLibrary)
    alias(libs.plugins.kotlin.plugin.parcelize)
}

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.app.shared"

        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.sebastianvm.contacts.features.base.Parcelize",
            )
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":routes"))
            implementation(project(":domain"))

            implementation(libs.bundles.ktorClient)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.retained)
            implementation(libs.circuit.overlay)
            implementation(libs.circuit.gestureNav)
            implementation(libs.circuit.codegen.annotations)
            implementation(libs.compose.material3.adaptiveNavigationSuite)
        }

        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }

        commonTest.dependencies {
            implementation(project(":fixtures"))
            implementation(libs.ktor.client.mock)
            implementation(libs.turbine)
            implementation(libs.circuit.test)
            implementation(libs.annotations)
        }
        named("androidHostTest") {
            dependencies {
                implementation(libs.testBalloon)
                implementation(libs.junit)
            }
        }
    }
}

metro {
    @OptIn(ExperimentalMetroGradleApi::class) enableCircuitCodegen.set(true)
}
