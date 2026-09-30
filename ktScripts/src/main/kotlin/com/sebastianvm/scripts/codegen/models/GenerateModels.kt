package com.sebastianvm.scripts.codegen.models

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.decodeFromStream
import com.github.ajalt.mordant.rendering.TextColors
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.OneToManyRelation
import com.sebastianvm.scripts.codegen.models.generators.DomainModelGenerator
import com.sebastianvm.scripts.codegen.models.generators.ExposedTableGenerator
import com.sebastianvm.scripts.codegen.models.generators.RouteGenerator
import com.sebastianvm.scripts.codegen.models.util.poet.lowercaseFirst
import com.sebastianvm.scripts.codegen.models.util.poet.writeTo
import com.sebastianvm.scripts.codegen.models.validation.ModelValidator
import com.sebastianvm.scripts.util.BaseCliktCommand
import com.sebastianvm.scripts.util.projectRoot
import com.sebastianvm.scripts.util.runCommand
import com.squareup.kotlinpoet.FileSpec
import java.io.File

class GenerateModels : BaseCliktCommand("models") {

    private val projectRoot by projectRoot()

    override fun run() {
        val modelFiles = File(projectRoot, Constants.Directories.YAML_MODELS)
        val rawModels = parseModels(modelFiles)
        ModelValidator().validateModels(modelDefinitions = rawModels)
        val models = processModels(modelDefinitions = rawModels)
        val domainModelFileSpecs = generateDomainModels(models)
        val serverTablesSpecs = generateServerDbTables(models)
        val routeSpecs = generateRoutes(models)

        saveFiles(
            listOf(
                domainModelFileSpecs to Constants.Modules.DOMAIN,
                serverTablesSpecs to Constants.Modules.SERVER,
                routeSpecs to Constants.Modules.ROUTES,
            )
        )

        "./gradlew spotlessApply".runCommand(File(projectRoot))
    }

    private fun parseModels(modelFiles: File): List<ModelDefinition> {
        return runStep(
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
    }

    private fun processModels(
        modelDefinitions: List<ModelDefinition>
    ): List<ProcessedModelDefinition> {
        return modelDefinitions.map { model ->
            val parentModels = modelDefinitions.filter {
                it.properties.any { prop ->
                    prop.schema is OneToManyRelation && prop.schema.model == model.name
                }
            }
            val foreignKeys = parentModels.associate {
                "${it.name.lowercaseFirst()}Id" to it.name
            }

            ProcessedModelDefinition.from(modelDefinition = model, foreignKeys = foreignKeys)
        }
    }

    private fun generateDomainModels(models: List<ProcessedModelDefinition>): List<FileSpec> {
        return runStep(
            action = {
                models.map { DomainModelGenerator(it).generateModel() }
            },
            successMessage = { "Successfully created domain models specs." },
        )
    }

    private fun generateServerDbTables(models: List<ProcessedModelDefinition>): List<FileSpec> {
        return runStep(
            action = {
                models.map { model ->
                    ExposedTableGenerator(modelDefinition = model).generateModel()
                }
            },
            successMessage = { "Successfully created Exposed database tables specs." },
        )
    }

    private fun generateRoutes(models: List<ProcessedModelDefinition>): List<FileSpec> {
        return runStep(
            action = {
                models.map { RouteGenerator(it).generateModel() }
            },
            successMessage = { "Successfully created routes." },
        )
    }

    private fun saveFiles(specsAndModules: List<Pair<List<FileSpec>, String>>) {
        runStep(
            action = {
                specsAndModules.forEach { (specs, module) ->
                    specs.forEach {
                        it.writeTo(module = module)
                    }
                }
            },
            successMessage = { "Successfully created files" },
        )
    }

    private fun <T> runStep(action: () -> T, successMessage: (T) -> String): T {
        val res = action()
        echo(TextColors.brightGreen(successMessage(res)))
        return res
    }
}
