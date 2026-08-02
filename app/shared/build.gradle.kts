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
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.circuit.foundation)
            implementation(libs.circuit.retained)
            implementation(libs.circuit.overlay)
            implementation(libs.circuit.gestureNav)
            implementation(libs.circuit.codegen.annotations)
        }

        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}

metro {
    @OptIn(ExperimentalMetroGradleApi::class) enableCircuitCodegen.set(true)
}
