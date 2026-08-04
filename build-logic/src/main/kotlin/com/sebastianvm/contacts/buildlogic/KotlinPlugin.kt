package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions

@OptIn(ExperimentalMetroGradleApi::class)
internal inline fun <reified E : HasConfigurableKotlinCompilerOptions<*>> Project.configureKotlin(
    isLibrary: Boolean,
    hasCompose: Boolean,
) {
    alias("metro")
    alias("testBalloon")

    configure<E> {
        this.configureKotlin(isLibrary)
    }
    configureDetekt(includeComposeRules = hasCompose)

    configure<MetroPluginExtension> {
        enableSuspendProviders.set(true)
    }
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
