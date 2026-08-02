package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import com.sebastianvm.contacts.buildlogic.extensions.library
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ClientModulePlugin(
    private val useCompose: Boolean,
    private val isMultiplatform: Boolean,
) : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (isMultiplatform) {
                alias("kotlinMultiplatform")
            }
            configurations.configureEach {
                resolutionStrategy.force(library("circuit-codegen-annotations").get())
            }
            if (useCompose) {
                alias("composeMultiplatform")
                alias("composeCompiler")
            }
        }
    }
}
