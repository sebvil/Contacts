package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import com.sebastianvm.contacts.buildlogic.extensions.library
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpComposeLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            alias("androidMultiplatformLibrary")

            ClientModulePlugin(useCompose = true, isMultiplatform = true).apply(target)
            AndroidPlugin(isMultiplatform = true).apply(target)

            configureWebTargets(isExecutable = false)
            configure<KotlinMultiplatformExtension> {
                jvm()

                sourceSets {
                    androidMain.dependencies {
                        implementation(library("compose.uiToolingPreview"))
                    }

                    commonMain.dependencies {
                        implementation(library("compose.runtime"))
                        implementation(library("compose.foundation"))
                        implementation(library("compose.material3"))
                        implementation(library("compose.ui"))
                        implementation(library("compose.components.resources"))
                        implementation(library("compose.uiToolingPreview"))
                    }
                }

                compilerOptions {
                    freeCompilerArgs.addAll("-Xexpect-actual-classes")
                }
            }

            dependencies {
                add("androidRuntimeClasspath", library("compose.uiTooling"))
            }
            configureKotlin<KotlinMultiplatformExtension>(isLibrary = true)
        }
    }
}
