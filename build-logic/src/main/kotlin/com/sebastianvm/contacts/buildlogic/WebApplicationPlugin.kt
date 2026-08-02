package com.sebastianvm.contacts.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class WebApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            ClientModulePlugin(useCompose = true, isMultiplatform = true).apply(target)
            configureWebTargets(isExecutable = true)
        }
    }
}

internal fun Project.configureWebTargets(isExecutable: Boolean) {
    configure<KotlinMultiplatformExtension> {
        js {
            browser()
            binaries.executable()
        }

        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
            binaries.executable()
        }
    }
    configureKotlin<KotlinMultiplatformExtension>(isLibrary = !isExecutable)
}
