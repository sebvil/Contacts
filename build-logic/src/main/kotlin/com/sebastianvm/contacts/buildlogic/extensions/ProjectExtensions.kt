package com.sebastianvm.contacts.buildlogic.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.library(lib: String) = libs.findLibrary(lib).get()

internal fun VersionCatalog.plugin(plugin: String): String = findPlugin(plugin).get().get().pluginId

internal fun Project.plugin(plugin: String) = libs.plugin(plugin)

internal fun Project.alias(plugin: String) {
    plugins.apply(plugin(plugin))
}

internal fun Project.bundle(bundle: String) = libs.findBundle(bundle).get()
