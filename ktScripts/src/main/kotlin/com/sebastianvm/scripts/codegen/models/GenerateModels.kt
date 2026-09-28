package com.sebastianvm.scripts.codegen.models

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import com.github.ajalt.mordant.rendering.TextColors
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.OneToManyRelation
import com.sebastianvm.scripts.codegen.models.generators.DomainModelGenerator
import com.sebastianvm.scripts.codegen.models.generators.ExposedTableGenerator
import com.sebastianvm.scripts.codegen.models.util.poet.writeTo
import com.sebastianvm.scripts.codegen.models.validation.ModelValidator
import com.sebastianvm.scripts.util.BaseCliktCommand
import com.sebastianvm.scripts.util.projectRoot
import com.sebastianvm.scripts.util.runCommand
import java.io.File

class GenerateModels : BaseCliktCommand("models") {

    private val projectRoot by projectRoot()

    override fun run() {
        val modelFiles = File(projectRoot, Constants.Directories.YAML_MODELS)
        val models =
            runStep(
                {
                    modelFiles
                        .walk()
                        .filter { it.extension == "yaml" }
                        .map {
                            Yaml(
                                    configuration =
                                        YamlConfiguration(
                                            polymorphismStyle = PolymorphismStyle.Property
                                        )
                                )
                                .decodeFromStream<ModelDefinition>(it.inputStream())
                        }
                        .toList()
                },
                { models -> "Successfully parsed ${models.size} models." },
            )

        ModelValidator().validateModels(models)

        val domainModelFileSpecs =
            runStep(
                action = {
                    models.map { DomainModelGenerator(it).generateModel() }
                },
                successMessage = { "Successfully created domain models specs." },
            )

        val serverTablesSpecs =
            runStep(
                action = {
                    models.map { model ->
                        val parentModels = models.filter {
                            it.properties.any { prop ->
                                prop.schema is OneToManyRelation && prop.schema.model == model.name
                            }
                        }
                        val foreignKeys = parentModels.associate {
                            "${it.name.replaceFirstChar { c -> c.lowercase() }}Id" to it.name
                        }
                        ExposedTableGenerator(modelDefinition = model, foreignKeys = foreignKeys)
                            .generateModel()
                    }
                },
                successMessage = { "Successfully created Exposed database tables specs." },
            )

        runStep(
            action = {
                domainModelFileSpecs.forEach {
                    it.writeTo(module = Constants.Modules.DOMAIN)
                }
                serverTablesSpecs.forEach {
                    it.writeTo(module = Constants.Modules.SERVER)
                }
            },
            successMessage = { "Successfully created files" },
        )

        "./gradlew spotlessApply".runCommand(File(projectRoot))
    }

    private fun <T> runStep(action: () -> T, successMessage: (T) -> String): T {
        val res = action()
        echo(TextColors.brightGreen(successMessage(res)))
        return res
    }
}
