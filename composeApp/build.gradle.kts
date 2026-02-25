import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.metro)
    alias(libs.plugins.parcelize)
}

ksp { arg("circuit.codegen.mode", "metro") }

kotlin {
    android {
        namespace = "com.sebastianvm.contacts.composeApp"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.get()))
            freeCompilerArgs.addAll(
                "-P",
                "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.sebastianvm.contacts.util.CommonParcelize",
            )
        }
        androidResources.enable = true
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain {
            kotlin {
                // needed so that common sources are picked up
                srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            }
            dependencies {
                implementation(libs.circuit.runtime)
                implementation(libs.circuit.foundation)
                implementation(libs.circuit.codegen.annotations)
                implementation(libs.circuit.gestureNav)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.metrox.vm)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.bundles.ktor.client)
                implementation(projects.shared)
                implementation(libs.androidx.datastore)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotest.assertions.core)
                implementation(libs.testBalloon)
                implementation(libs.circuit.test)
                implementation(libs.ktor.clientMock)
            }
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.uiTooling)
    dependencies { add("kspCommonMainMetadata", libs.circuit.codegen) }
}

compose.desktop {
    application {
        mainClass = "com.sebastianvm.contacts.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.sebastianvm.contacts"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    if (this is AbstractKotlinCompile<*>) {
        // Disable incremental in this project because we're generating top-level declarations
        // Required for Circuit codegen to work
        incremental = false
    }
    dependsOn("kspCommonMainKotlinMetadata")
}
