package com.sebastianvm.scripts.formatters.versions.model

/** Sections are distinct groups within a table, separated by a blank line. */
data class TomlTableSection<T : VersionCatalogEntry>(
    val values: List<T>,
    val comments: List<String>,
) {

    class Builder<T : VersionCatalogEntry> {
        val values: MutableList<T> = mutableListOf()
        val comments: MutableList<String> = mutableListOf()

        fun build(): TomlTableSection<T> =
            TomlTableSection(values.sortedBy { it.name }, comments = comments.toList())
    }
}
