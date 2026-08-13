package com.sebastianvm.contacts.buildlogic

import com.sebastianvm.contacts.buildlogic.extensions.alias
import com.sebastianvm.contacts.buildlogic.extensions.bundle
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.configureKmpLibrary(useCompose = false)
    }
}

/**
 * Shared scaffolding for KMP library convention plugins: Android/JVM/web targets, Kotlin/Detekt
 * setup, and unit test dependencies. [useCompose] controls whether Compose Multiplatform is
 * applied; [additionalConfiguration] runs afterward for anything specific to a given plugin (e.g.
 * Compose-only dependencies).
 */
internal fun Project.configureKmpLibrary(
    useCompose: Boolean,
    additionalConfiguration: Project.() -> Unit = {},
) {
    alias("androidMultiplatformLibrary")

    ClientModulePlugin(useCompose = useCompose, isMultiplatform = true).apply(this)
    AndroidPlugin(isMultiplatform = true).apply(this)

    configureWebTargets(isExecutable = false)
    configure<KotlinMultiplatformExtension> {
        jvm {
            configureJava(this@configureKmpLibrary)
        }

        compilerOptions {
            freeCompilerArgs.addAll("-Xexpect-actual-classes")
        }

        sourceSets {
            commonTest {
                dependencies {
                    implementation(bundle("unitTests"))
                }
            }
        }
    }

    additionalConfiguration()

    configureKotlin<KotlinMultiplatformExtension>(isLibrary = true, hasCompose = useCompose)
}
