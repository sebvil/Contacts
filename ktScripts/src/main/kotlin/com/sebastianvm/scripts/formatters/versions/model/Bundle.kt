package com.sebastianvm.scripts.formatters.versions.model

data class Bundle(
    override val name: String,
    val libraries: List<String>,
    override val comments: List<String>,
) : VersionCatalogEntry {

    class Builder(val name: String, val comments: List<String>) {
        val libraries: MutableList<String> = mutableListOf()

        fun build(): Bundle {
            return Bundle(name = name, libraries = libraries.toList(), comments = comments)
        }
    }
}
