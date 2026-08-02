package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.bundle
import com.sebastianvm.contacts.buildlogic.extensions.libs
import com.sebastianvm.contacts.buildlogic.extensions.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

internal inline fun <
        reified E : HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>
        > Project.configureJavaAndKotlin(isLibrary: Boolean, hasCompose: Boolean) {
    configure<E> {
        configureJava(this@configureJavaAndKotlin)
    }
    configureKotlin<E>(isLibrary = isLibrary, hasCompose = hasCompose)

    dependencies {
        testImplementation(bundle("unitTests"))
    }
}

internal inline fun <reified E : HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions>> E.configureJava(target: Project) {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(target.libs.findVersion("java").get().toString()))
    }
}
