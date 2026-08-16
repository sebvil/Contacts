package com.sebastianvm.scripts.codegen.module

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.transformAll
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.mordant.terminal.YesNoPrompt
import com.sebastianvm.scripts.util.projectRoot
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.pathString
import kotlin.io.path.writeText
import org.intellij.lang.annotations.Language

class MakeModule : CliktCommand() {

    private val name by argument().transformAll { it.last().trim(':') }
    private val moduleType: ModuleType by option(help = "Module type").enum<ModuleType>().required()
    private val projectRoot by projectRoot()

    override fun run() {
        echo("Creating $moduleType module $name")

        val directoriesToCreate = mutableListOf<Path>()

        val directoryRoot = Path.of(projectRoot, name.replace(":", File.separator))
        val contentRoot =
            Path.of(
                directoryRoot.pathString,
                "src/commonMain/kotlin",
                packageParts().joinToString(File.separator),
            )
        directoriesToCreate.addAll(listOf(directoryRoot, contentRoot))

        val gradleFile = Path(directoryRoot.pathString, "build.gradle.kts")

        echo("Will create the following files and directories:")
        directoriesToCreate.forEach {
            echo(it.pathString + '/')
        }
        echo(gradleFile)
        val continueCreating =
            YesNoPrompt(prompt = "Continue?", default = false, terminal = terminal).ask()
        if (continueCreating == true) {
            directoriesToCreate.forEach { it.createDirectories() }
            gradleFile.writeText(makeBuildGradleFileContent())
        }
    }

    private fun packageParts(): List<String> {
        val moduleParts = name.split(":")
        return listOf("com", "sebastianvm") +
            if (moduleParts.first() == "app") {
                listOf("contacts") + moduleParts.subList(1, moduleParts.size)
            } else {
                moduleParts
            }
    }

    @Language("kts")
    private fun makeBuildGradleFileContent(): String {
        val conventionPluginName =
            when (moduleType) {
                Compose -> "kmpComposeLibrary"
                Kmp -> "kmpLibrary"
            }
        return """
        plugins {
            alias(libs.plugins.$conventionPluginName)
        }
        
        kotlin {
            android {
                namespace = "${packageParts().joinToString(".")}"
            }
        }
        """
            .trimIndent()
    }
}

/**
 * Type of module. Only supporting library modules for now as there is no need to create more app
 * targets.
 */
private enum class ModuleType {
    Compose,
    Kmp,
}
