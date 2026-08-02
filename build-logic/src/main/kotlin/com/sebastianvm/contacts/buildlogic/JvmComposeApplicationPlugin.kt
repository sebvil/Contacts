package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.implementation
import com.sebastianvm.contacts.buildlogic.extensions.library
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

internal inline fun <
    reified E : HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>
> Project.configureJvmComposeApplication() {
    ClientModulePlugin(useCompose = true, isMultiplatform = false).apply(this)
    configureJavaAndKotlin<E>(isLibrary = false, hasCompose = true)
    dependencies {
        implementation(project(":app:shared"))
        implementation(library("compose.uiToolingPreview"))
    }
}
