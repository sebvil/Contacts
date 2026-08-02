package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

internal inline fun <
    reified E : HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>
> Project.configureJavaAndKotlin(isLibrary: Boolean, hasCompose: Boolean) {
    configure<E> {
        configureJavaAndKotlin(this@configureJavaAndKotlin)
        configureKotlin<E>(isLibrary = isLibrary, hasCompose = hasCompose)
    }
}

internal inline fun <reified E : HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>> E
    .configureJavaAndKotlin(target: Project) {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(target.libs.findVersion("java").get().toString()))
    }
}
