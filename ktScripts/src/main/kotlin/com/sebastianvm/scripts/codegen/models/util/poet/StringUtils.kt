package com.sebastianvm.scripts.codegen.models.util.poet

fun pluralize(string: String): String {
    return when {
        string.endsWith("s") -> "${string}es"
        string.endsWith("y") -> "${string.dropLast(1)}ies"
        else -> "${string}s"
    }
}

fun String.lowercaseFirst() = replaceFirstChar { it.lowercase() }

fun String.capitalizeFirst() = replaceFirstChar { it.uppercase() }

fun tableName(modelName: String) = "${pluralize(modelName)}Table"
