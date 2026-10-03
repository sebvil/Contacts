package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.processed.responseClassName
import com.sebastianvm.scripts.codegen.models.util.poet.fileSpecBuilder
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec

class ServerRepositoryGenerator(private val modelDefinition: ProcessedModelDefinition) : Generator {

    override fun generate(): FileSpec {
        return fileSpecBuilder(
                packageName = Constants.Packages.REPOSITORY,
                fileName = modelDefinition.repositoryName,
            )
            .addMapperMethod()
            .build()
    }

    private fun FileSpec.Builder.addMapperMethod(): FileSpec.Builder {
        return addFunction(
            FunSpec.builder("to${modelDefinition.responseName}")
                .receiver(Constants.Types.EXPOSED_RESULT_ROW)
                .returns(modelDefinition.responseClassName)
                .apply {
                    addParameters(
                        modelDefinition.relationshipProperties.map {
                            ParameterSpec.builder(name = it.name, it.responseType).build()
                        }
                    )
                }
                .addCode(
                    CodeBlock.builder()
                        .addStatement("return %T(", modelDefinition.responseClassName)
                        .indent()
                        .apply {
                            modelDefinition.allProperties
                                .filter { !it.isForeignKey }
                                .forEach {
                                    add(it.resultRowMapperInitializer)
                                    add("\n")
                                }
                        }
                        .unindent()
                        .add(")")
                        .build()
                )
                .build()
        )
    }
}
