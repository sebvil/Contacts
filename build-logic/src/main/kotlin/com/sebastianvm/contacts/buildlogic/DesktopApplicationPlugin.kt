package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal class DesktopApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            alias("kotlinJvm")
            configureJvmComposeApplication<KotlinJvmProjectExtension>()
        }
    }
}
