package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedPropertyDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.addSerializableAnnotation
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

class ModelRequestGenerator(private val modelDefinition: ProcessedModelDefinition) : Generator {

    override fun generate(): FileSpec {
        return DataClassDefinition(
                packageName = Constants.Packages.DTO,
                className = modelDefinition.requestName,
                properties =
                    modelDefinition.allProperties
                        .filter {
                            (it as? ProcessedPropertyDefinition.ModelProperty)?.isForeignKey != true
                        }
                        .map {
                            DataClassPropertyDefinition(
                                propertyName = it.name,
                                type = Constants.Types.OPTION.parameterizedBy(it.requestType),
                                description = it.description,
                                defaultValue = CodeBlock.of("%T", Constants.Types.NONE),
                            )
                        },
                description = modelDefinition.description,
            )
            .toFileSpec {
                addSerializableAnnotation()
            }
    }
}
