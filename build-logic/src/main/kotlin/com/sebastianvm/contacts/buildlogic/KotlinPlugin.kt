package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import com.sebastianvm.contacts.buildlogic.extensions.library
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.extensions.FailOnSeverity
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions

internal inline fun <reified E : HasConfigurableKotlinCompilerOptions<*>> Project.configureKotlin(
    isLibrary: Boolean
) {
    alias("detekt")

    configure<E> {
        this.configureKotlin(isLibrary)
    }

    configure<DetektExtension> {
        buildUponDefaultConfig.set(true)
        failOnSeverity.set(FailOnSeverity.Info)
        config.setFrom(
            files(
                "${rootDir}/config/detekt/detekt-compose.yml",
                "${rootDir}/config/detekt/detekt.yml",
            )
        )
    }

    tasks.named("check").configure {
        setDependsOn(
            tasks.named { name ->
                "detekt" in name.lowercase() &&
                    "baseline" !in name.lowercase() &&
                    name != "detektGenerateConfig"
            }
        )
    }
    tasks.withType<Detekt>().configureEach {
        // `exclude("**/build/**")` doesn't work here: detekt gives each Kotlin source
        // directory its own FileTree root, so generated-source roots that live inside
        // `build/` (e.g. KSP output) never have a "build" segment in their *relative*
        // path, and the glob never matches. Filter the resolved files instead.
        val buildDirPath = layout.buildDirectory.get().asFile.toPath()
        setSource(source.filter { file -> !file.toPath().startsWith(buildDirPath) })
    }

    dependencies {
        add("detektPlugins", library("detekt.compose"))
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
