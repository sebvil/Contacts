package com.sebastianvm.contacts.buildlogic.extensions

import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    add("implementation", dependencyNotation)
}

internal fun DependencyHandlerScope.testImplementation(dependencyNotation: Any) {
    add("testImplementation", dependencyNotation)
}
