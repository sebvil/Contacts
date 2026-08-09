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
            // Compose UI tests on the js target require an executable binary to bundle the
            // Skiko runtime with webpack, even for library modules with no `main()`.
            // https://youtrack.jetbrains.com/issue/CMP-4906
            binaries.executable()
        }

        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
            browser()
            if (isExecutable) binaries.executable()
        }
    }
    configureKotlin<KotlinMultiplatformExtension>(isLibrary = !isExecutable, hasCompose = true)
}
