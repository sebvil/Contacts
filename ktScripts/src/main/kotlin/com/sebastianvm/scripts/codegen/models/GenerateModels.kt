package com.sebastianvm.scripts.codegen.models

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.mordant.rendering.TextColors
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.generators.DomainModelGenerator
import com.sebastianvm.scripts.codegen.models.util.poet.writeTo
import com.sebastianvm.scripts.util.projectRoot
import java.io.File

class GenerateModels : CliktCommand("models") {

    private val projectRoot by projectRoot()

    override fun run() {
        val modelFiles = File(projectRoot, Constants.Directories.YAML_MODELS)
        val models =
            modelFiles
                .walk()
                .filter { it.extension == "yaml" }
                .map {
                    Yaml(
                            configuration =
                                YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property)
                        )
                        .decodeFromStream<ModelDefinition>(it.inputStream())
                }
                .toList()

        echo(TextColors.brightGreen("Successfully parsed ${models.size} models."))

        val domainModelFileSpecs = models.map { DomainModelGenerator(it).generateModel() }
        echo(TextColors.brightGreen("Successfully created domain models specs."))

        domainModelFileSpecs.forEach {
            it.writeTo(module = Constants.Modules.DOMAIN)
        }

        echo(TextColors.brightGreen("Successfully created files"))
    }
}
