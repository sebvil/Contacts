package com.sebastianvm.scripts.formatters.versions

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.sebastianvm.scripts.formatters.versions.model.TomlTableSection
import com.sebastianvm.scripts.formatters.versions.model.VersionCatalogEntry
import com.sebastianvm.scripts.formatters.versions.parser.VersionCatalogsParser
import com.sebastianvm.scripts.util.projectRoot
import java.io.File

class FormatVersionCatalogs : CliktCommand(name = "versions") {

    private val dryRun by
        option(
                "--dryRun",
                "-n",
                help = "Prints out the formatted file without actually overwriting it",
            )
            .flag(default = false)
    private val projectRoot by projectRoot()

    override fun run() {
        echo("Formatting version catalogs")
        val catalogPath = "$projectRoot/gradle/libs.versions.toml"
        echo("Reading $catalogPath")
        val versionCatalogsFile = File(catalogPath)
        val fileLines = versionCatalogsFile.readLines()
        val catalogs = VersionCatalogsParser().parse(catalogPath, fileLines)
        val newCatalogs = buildString {
            appendTable("versions", catalogs.versions) { version ->
                appendLine("""${version.name} = "${version.value}"""")
            }

            appendTable("libraries", catalogs.libraries) { library ->
                appendLine(
                    """${library.name} = { module = "${library.module}", version.ref = "${library.version}" }"""
                )
            }

            appendTable("bundles", catalogs.bundles) { bundle ->
                appendLine("${bundle.name} = [")
                appendLine(bundle.libraries.joinToString(separator = ",\n") { """   "$it"""" })
                appendLine("]")
            }

            appendTable("plugins", catalogs.plugins) { plugin ->
                if (plugin.version != null) {
                    appendLine(
                        """${plugin.name} = { id = "${plugin.id}", version.ref = "${plugin.version}" }"""
                    )
                } else {
                    appendLine("""${plugin.name} = { id = "${plugin.id}" }""")
                }
            }
        }

        if (dryRun) {
            echo(newCatalogs)
        } else {
            versionCatalogsFile.writeText(newCatalogs)
            echo("Successfully formatted $catalogPath")
        }
    }

    private fun <T : VersionCatalogEntry> StringBuilder.appendTable(
        name: String,
        table: List<TomlTableSection<T>>,
        builder: StringBuilder.(T) -> Unit,
    ) {
        appendLine("[$name]")
        table.forEach { section ->
            appendSection(section, builder)
        }
    }

    private fun <T : VersionCatalogEntry> StringBuilder.appendSection(
        section: TomlTableSection<T>,
        builder: StringBuilder.(T) -> Unit,
    ) {
        section.comments.forEach {
            appendLine(it)
        }
        section.values.forEach { value ->
            value.comments.forEach {
                appendLine(it)
            }
            builder(value)
        }
        appendLine()
    }
}
