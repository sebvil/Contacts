package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions

internal inline fun <reified E : HasConfigurableKotlinCompilerOptions<*>> Project.configureKotlin(
    isLibrary: Boolean,
    hasCompose: Boolean,
) {
    alias("metro")

    configure<E> {
        this.configureKotlin(isLibrary)
    }
    configureDetekt(includeComposeRules = hasCompose)
}

private inline fun <reified E : HasConfigurableKotlinCompilerOptions<*>> E.configureKotlin(
    isLibrary: Boolean
) {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Wextra",
            "-Xintrinsic-const-evaluation",
            "-Xconsistent-data-class-copy-visibility",
            "-Xreturn-value-checker=full",
            // errors rn, try later
            // "-Xallow-returns-result-of",
            "-Werror",
        )
        if (isLibrary) {
            freeCompilerArgs.add("-Xexplicit-api=strict")
        }
    }
}
