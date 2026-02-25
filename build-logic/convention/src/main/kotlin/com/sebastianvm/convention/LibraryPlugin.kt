package com.sebastianvm.convention

import com.sebastianvm.convention.util.apply
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension

inline fun <reified E> Project.configureLibrary()
    where E : KotlinBaseExtension, E : HasConfigurableKotlinCompilerOptions<*> {
    configureDetekt()
    apply("testBalloon")
    configureKotlin<E>()
}
