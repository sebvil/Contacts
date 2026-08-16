package com.sebastianvm.scripts.formatters.versions.model

data class Plugin(
    override val name: String,
    val id: String,
    val version: String?,
    override val comments: List<String>,
) : VersionCatalogEntry
