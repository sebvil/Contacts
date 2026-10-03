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
import com.sebastianvm.scripts.codegen.models.generators.Generator
import com.sebastianvm.scripts.codegen.models.generators.ModelRequestGenerator
import com.sebastianvm.scripts.codegen.models.generators.ModelResponseGenerator
import com.sebastianvm.scripts.codegen.models.generators.RouteGenerator
import com.sebastianvm.scripts.codegen.models.generators.ServerRepositoryGenerator
import com.sebastianvm.scripts.codegen.models.util.poet.lowercaseFirst
import com.sebastianvm.scripts.codegen.models.util.poet.writeTo
import com.sebastianvm.scripts.codegen.models.validation.ModelValidator
import com.sebastianvm.scripts.util.BaseCliktCommand
import com.sebastianvm.scripts.util.EchoHandler
import com.sebastianvm.scripts.util.projectRoot
import com.sebastianvm.scripts.util.runCommand
import com.squareup.kotlinpoet.FileSpec
import java.io.File

class GenerateModels : BaseCliktCommand("models") {

    private val projectRoot by projectRoot()

    override fun run() {
        val modelFiles = File(projectRoot, Constants.Directories.YAML_MODELS)
        val rawModels = parseModels(modelFiles)
        val models = processModels(modelDefinitions = rawModels)
        createFiles(models)

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

    private fun createFiles(models: List<ProcessedModelDefinition>) {
        val generationInputs =
            listOf(
                GenerationInputs(
                    factory = ::DomainModelGenerator,
                    successMessage = "Domain model specs created.",
                    module = Constants.Modules.DOMAIN,
                ),
                GenerationInputs(
                    factory = ::ExposedTableGenerator,
                    successMessage = "Exposed tables specs created.",
                    module = Constants.Modules.SERVER,
                ),
                GenerationInputs(
                    factory = ::RouteGenerator,
                    successMessage = "Exposed tables specs created.",
                    module = Constants.Modules.ROUTES,
                ),
                GenerationInputs(
                    factory = ::ModelRequestGenerator,
                    successMessage = "Request model specs created.",
                    module = Constants.Modules.ROUTES,
                ),
                GenerationInputs(
                    factory = ::ModelResponseGenerator,
                    successMessage = "Response model specs created.",
                    module = Constants.Modules.ROUTES,
                ),
                GenerationInputs(
                    factory = ::ServerRepositoryGenerator,
                    successMessage = "Repository specs created.",
                    module = Constants.Modules.SERVER,
                ),
            )

        val generationOutputs = generateFiles(generationInputs, models)
        saveFiles(generationOutputs)
    }

    private fun generateFiles(
        generationInputs: List<GenerationInputs>,
        models: List<ProcessedModelDefinition>,
    ): List<GenerationOutputs> {
        return generationInputs.map { inputs ->
            val specs =
                runStep(
                    action = {
                        models.map { inputs.factory(it).generate() }
                    },
                    successMessage = { inputs.successMessage },
                )
            GenerationOutputs(specs = specs, module = inputs.module)
        }
    }

    private fun saveFiles(specsAndModules: List<GenerationOutputs>) {
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

    data class GenerationInputs(
        val factory: (ProcessedModelDefinition) -> Generator,
        val successMessage: String,
        val module: String,
    )

    data class GenerationOutputs(
        val specs: List<FileSpec>,
        val module: String,
    )

    companion object {
        context(_: EchoHandler)
        fun processModels(modelDefinitions: List<ModelDefinition>): List<ProcessedModelDefinition> {
            ModelValidator().validateModels(modelDefinitions = modelDefinitions)
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
    }
}
