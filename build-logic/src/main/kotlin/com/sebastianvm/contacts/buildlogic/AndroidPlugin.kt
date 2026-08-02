package com.sebastianvm.contacts.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.sebastianvm.contacts.buildlogic.extensions.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class AndroidPlugin(val isMultiplatform: Boolean) : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            if (isMultiplatform) {
                configure<KotlinMultiplatformExtension> {
                    (this as ExtensionAware).extensions.configure<
                        KotlinMultiplatformAndroidLibraryTarget
                    >(
                        "android"
                    ) {
                        compileSdk = compileSdk()
                        minSdk = minSdk()

                        androidResources {
                            enable = true
                        }
                        withHostTest {
                            isIncludeAndroidResources = true
                        }
                        this.configureJava(target)
                    }
                }
            } else {
                configure<ApplicationExtension> {
                    compileSdk = compileSdk()
                    defaultConfig {
                        minSdk = minSdk()
                        targetSdk = targetSdk()
                    }

                    compileOptions {
                        val javaVersion =
                            JavaVersion.entries
                                .toTypedArray()[
                                    libs.findVersion("java").get().toString().toInt() - 1]
                        sourceCompatibility = javaVersion
                        targetCompatibility = javaVersion
                    }
                }
                configureJvmComposeApplication<KotlinAndroidProjectExtension>()
            }
        }
    }

    private fun Project.compileSdk() =
        libs.findVersion("android.compileSdk").get().toString().toInt()

    private fun Project.minSdk() = libs.findVersion("android.minSdk").get().toString().toInt()

    private fun Project.targetSdk() = libs.findVersion("android.targetSdk").get().toString().toInt()
}
