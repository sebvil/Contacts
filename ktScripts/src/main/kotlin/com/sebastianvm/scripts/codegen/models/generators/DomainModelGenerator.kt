package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.sebastianvm.scripts.util.EchoHandler
import com.squareup.kotlinpoet.FileSpec

class DomainModelGenerator(private val modelDefinition: ModelDefinition) {

    context(_: EchoHandler)
    fun generateModel(): FileSpec {
        val dataClassDefinition =
            DataClassDefinition(
                packageName = Constants.Packages.DOMAIN_MODELS,
                className = modelDefinition.name,
                properties =
                    modelDefinition.properties.map {
                        DataClassPropertyDefinition(
                            propertyName = it.name,
                            type = it.schema.className,
                            description = it.description,
                        )
                    },
                description = modelDefinition.description,
            )
        return dataClassDefinition.toFileSpec()
    }
}
