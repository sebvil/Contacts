package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.sebastianvm.scripts.util.EchoHandler
import com.squareup.kotlinpoet.FileSpec

class DomainModelGenerator(private val modelDefinition: ProcessedModelDefinition) {

    context(_: EchoHandler)
    fun generateModel(): FileSpec {
        val dataClassDefinition =
            DataClassDefinition(
                packageName = Constants.Packages.DOMAIN_MODELS,
                className = modelDefinition.domainModelName,
                properties =
                    modelDefinition.allProperties.map {
                        DataClassPropertyDefinition(
                            propertyName = it.name,
                            type = it.domainModelType,
                            description = it.description,
                            defaultValue = it.domainModelDefaultValue,
                        )
                    },
                description = modelDefinition.description,
            )
        return dataClassDefinition.toFileSpec()
    }
}
