package com.sebastianvm.scripts.codegen.models.util.poet

fun pluralize(string: String): String {
    return when {
        string.endsWith("s") -> "${string}es"
        string.endsWith("y") -> "${string.dropLast(1)}ies"
        else -> "${string}s"
    }
}
