package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            alias("androidMultiplatformLibrary")

            ClientModulePlugin(useCompose = false, isMultiplatform = true).apply(target)
            AndroidPlugin(isMultiplatform = true).apply(target)

            configureWebTargets(isExecutable = false)
            configure<KotlinMultiplatformExtension> {
                jvm {
                    configureJava(this@with)
                }

                compilerOptions {
                    freeCompilerArgs.addAll("-Xexpect-actual-classes")
                }
            }

            configureKotlin<KotlinMultiplatformExtension>(isLibrary = true, hasCompose = false)
        }
    }
}
