package com.sebastianvm.scripts.formatters.versions.model

data class Library(override val name: String, val module: String, val version: String, override val comments: List<String>) : VersionCatalogEntry