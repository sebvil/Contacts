package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply("kotlinMultiplatform")
            apply("androidLibrary")
            configureLibrary<KotlinMultiplatformExtension>()
        }
    }
}
