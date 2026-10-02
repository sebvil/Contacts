package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.addSerializableAnnotation
import com.squareup.kotlinpoet.FileSpec

class ModelResponseGenerator(private val modelDefinition: ProcessedModelDefinition) : Generator {

    override fun generate(): FileSpec {
        return DataClassDefinition(
                packageName = Constants.Packages.DTO,
                className = modelDefinition.responseName,
                properties =
                    modelDefinition.allProperties
                        .filter { !it.isForeignKey }
                        .map {
                            DataClassPropertyDefinition(
                                propertyName = it.name,
                                type = it.responseType,
                                description = it.description,
                                defaultValue = it.responseDefaultValue,
                            )
                        },
                description = modelDefinition.description,
            )
            .toFileSpec {
                addSerializableAnnotation()
            }
    }
}
