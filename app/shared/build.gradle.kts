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
            api(project(":app:database"))
            implementation(libs.sqldelight.coroutines)

            implementation(libs.bundles.ktorClient)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.retained)
            implementation(libs.circuit.overlay)
            implementation(libs.circuit.gestureNav)
            implementation(libs.circuit.codegen.annotations)
            implementation(libs.compose.material3.adaptiveNavigationSuite)
            implementation(libs.compose.material3.adaptive)
        }

        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }

        webMain.dependencies {
            // CIO (used on jvm/android) requires a real Node.js `net` module, which browsers
            // don't have. The Js engine wraps `fetch` instead, which browsers support natively.
            implementation(libs.ktor.client.js)
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
