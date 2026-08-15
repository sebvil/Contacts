package com.sebastianvm.scripts.formatters.versions.model


data class Version(override val name: String, val value: String, override val comments: List<String>) :
    VersionCatalogEntry
