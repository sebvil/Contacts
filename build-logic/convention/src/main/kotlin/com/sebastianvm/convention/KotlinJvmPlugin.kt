package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension

class KotlinJvmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply("kotlinJvm")
            configureLibrary<KotlinJvmExtension>()
        }
    }
}
