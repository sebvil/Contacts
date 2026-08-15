package com.sebastianvm.scripts.formatters.versions.model


sealed interface VersionCatalogEntry {
    val name: String
    val comments: List<String>
}