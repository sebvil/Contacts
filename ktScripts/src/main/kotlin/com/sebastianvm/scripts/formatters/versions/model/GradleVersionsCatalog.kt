package com.sebastianvm.scripts.formatters.versions.model

data class GradleVersionsCatalog(
    val versions: List<TomlTableSection<Version>>,
    val libraries: List<TomlTableSection<Library>>,
    val bundles: List<TomlTableSection<Bundle>>,
    val plugins: List<TomlTableSection<Plugin>>,

    ) {

    class Builder {
        var versions: List<TomlTableSection<Version>> = emptyList()
        var libraries: List<TomlTableSection<Library>> = emptyList()
        var bundles: List<TomlTableSection<Bundle>> = emptyList()
        var plugins: List<TomlTableSection<Plugin>> = emptyList()

        fun build(): GradleVersionsCatalog {
            return GradleVersionsCatalog(
                versions = versions,
                libraries = libraries,
                bundles = bundles,
                plugins = plugins,
            )
        }

    }
}
