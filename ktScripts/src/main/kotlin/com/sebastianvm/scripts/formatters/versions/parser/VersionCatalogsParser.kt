@file:Suppress("UnsafeCallOnNullableType")

package com.sebastianvm.scripts.formatters.versions.parser

import com.github.ajalt.clikt.core.CliktCommand
import com.sebastianvm.scripts.formatters.versions.model.Bundle
import com.sebastianvm.scripts.formatters.versions.model.GradleVersionsCatalog
import com.sebastianvm.scripts.formatters.versions.model.Library
import com.sebastianvm.scripts.formatters.versions.model.Plugin
import com.sebastianvm.scripts.formatters.versions.model.TomlTableSection
import com.sebastianvm.scripts.formatters.versions.model.Version
import com.sebastianvm.scripts.formatters.versions.model.VersionCatalogEntry
import org.intellij.lang.annotations.Language

class VersionCatalogsParser {
    private var currentLineIndex = 0

    context(command: CliktCommand)
    fun parse(fileName: String, lines: List<String>): GradleVersionsCatalog {
        return runCatching {
                GradleVersionsCatalog.Builder()
                    .apply {
                        while (currentLineIndex < lines.size) {
                            val line = lines[currentLineIndex]
                            val tableName = TableName.valueOf(line.trim('[', ']').uppercase())
                            when (tableName) {
                                TableName.VERSIONS -> versions = parseVersions(lines)
                                TableName.LIBRARIES -> libraries = parseLibraries(lines)
                                TableName.BUNDLES -> bundles = parseBundles(lines)
                                TableName.PLUGINS -> plugins = parsePlugins(lines)
                            }
                        }
                    }
                    .build()
            }
            .onFailure {
                command.echo("Error parsing $fileName:${currentLineIndex+1}")
            }
            .getOrThrow()
    }

    private fun parseVersions(lines: List<String>): List<TomlTableSection<Version>> =
        parseSingleLineElementsTable(lines, """(?<name>.*)="(?<version>.*)"""") { groups, comments
            ->
            Version(
                name = groups["name"]!!.value,
                value = groups["version"]!!.value,
                comments = comments.toList(),
            )
        }

    private fun parseLibraries(lines: List<String>): List<TomlTableSection<Library>> =
        parseSingleLineElementsTable(
            lines,
            """(?<name>.*)=\{module="(?<module>.*)",version.ref="(?<version>.*)"}""",
        ) { groups, comments ->
            Library(
                name = groups["name"]!!.value,
                module = groups["module"]!!.value,
                version = groups["version"]!!.value,
                comments = comments.toList(),
            )
        }

    private fun parseBundles(lines: List<String>): List<TomlTableSection<Bundle>> {
        return parseTable(lines) { comments ->
            val bundleLines = mutableListOf<String>()
            var currentLine = lines[currentLineIndex].trim()
            while (!currentLine.endsWith("]")) {
                bundleLines.add(currentLine)
                currentLineIndex++
                currentLine = lines[currentLineIndex]
            }
            val bundleString =
                bundleLines.joinToString(separator = "").replace(Regex("""["\s\n]"""), "") + "]"
            val parsedBundle =
                Regex("""(?<name>.*)=\[(?<libraries>.*)]""").find(bundleString)
                    ?: error("Could not parse bundle: $bundleString")
            Bundle(
                name = parsedBundle.groups["name"]!!.value,
                libraries = parsedBundle.groups["libraries"]!!.value.split(",").sorted(),
                comments = comments.toList(),
            )
        }
    }

    private fun parsePlugins(lines: List<String>): List<TomlTableSection<Plugin>> =
        parseSingleLineElementsTable(
            lines,
            """(?<name>.*)=\{id="(?<id>[A-z.]+)"(,version.ref="(?<version>.*)")?}""",
        ) { groups, comments ->
            Plugin(
                name = groups["name"]!!.value,
                id = groups["id"]!!.value,
                version = groups["version"]?.value,
                comments = comments.toList(),
            )
        }

    private fun <T : VersionCatalogEntry> parseSingleLineElementsTable(
        lines: List<String>,
        @Language("RegExp") vararg regex: String,
        buildElement: (groups: MatchGroupCollection, comments: List<String>) -> T,
    ): List<TomlTableSection<T>> {
        return parseTable(lines) { comments ->
            val currentLine = lines[currentLineIndex]
            val strippedLine = currentLine.replace(" ", "")
            val match =
                regex.firstNotNullOfOrNull {
                    Regex(it).find(strippedLine)
                } ?: error("Couldn't parse $currentLine at $currentLine")
            buildElement(match.groups, comments)
        }
    }

    private fun <T : VersionCatalogEntry> parseTable(
        lines: List<String>,
        buildElement: (comments: List<String>) -> T,
    ): List<TomlTableSection<T>> {
        currentLineIndex++
        var currentLine = lines[currentLineIndex]
        val sections = mutableListOf<TomlTableSection<T>>()
        var currentSectionBuilder = TomlTableSection.Builder<T>()
        val comments = mutableListOf<String>()
        while (!currentLine.startsWith("[") && currentLineIndex < lines.size) {
            when {
                currentLine.isBlank() -> {
                    if (currentSectionBuilder.values.isNotEmpty()) {
                        sections.add(currentSectionBuilder.build())
                        currentSectionBuilder = TomlTableSection.Builder()
                    }
                }

                currentLine.startsWith("##") -> {
                    currentSectionBuilder.comments.add(currentLine)
                }

                currentLine.startsWith("#") -> {
                    comments.add(currentLine)
                }

                else -> {
                    val element = buildElement(comments)
                    currentSectionBuilder.values.add(element)
                    comments.clear()
                }
            }
            currentLineIndex++
            if (currentLineIndex < lines.size) {
                currentLine = lines[currentLineIndex]
            }
        }
        if (currentSectionBuilder.values.isNotEmpty()) {
            sections.add(currentSectionBuilder.build())
        }
        return sections
    }

    private enum class TableName {
        VERSIONS,
        LIBRARIES,
        BUNDLES,
        PLUGINS,
    }
}
