package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

internal inline fun <
    reified E : HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>
> Project.configureJvm() {
    configure<E> {
        configureJvm(this@configureJvm)
    }
}

internal inline fun <reified E : HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>> E
    .configureJvm(target: Project) {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(target.libs.findVersion("java").get().toString()))
    }
}
