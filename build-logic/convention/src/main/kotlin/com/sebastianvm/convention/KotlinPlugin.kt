package com.sebastianvm.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

inline fun <reified E> Project.configureKotlin()
    where E : KotlinBaseExtension, E : HasConfigurableKotlinCompilerOptions<*> {
    configure<E> {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Werror",
                "-Xcontext-parameters",
                "-Xcontext-sensitive-resolution",
            )
            optIn.add("kotlin.uuid.ExperimentalUuidApi")
        }
    }
}
