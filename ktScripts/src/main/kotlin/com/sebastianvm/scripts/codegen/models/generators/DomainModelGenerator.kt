package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.toTypeName
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.squareup.kotlinpoet.FileSpec

class DomainModelGenerator(private val modelDefinition: ModelDefinition) {

    fun generateModel(): FileSpec {
        val dataClassDefinition =
            DataClassDefinition(
                packageName = Constants.Packages.DOMAIN_MODELS,
                className = modelDefinition.name,
                properties =
                    modelDefinition.properties.map {
                        DataClassPropertyDefinition(
                            propertyName = it.key,
                            type = it.value.toTypeName(),
                            description = "",
                        )
                    },
                description = "",
            )
        return dataClassDefinition.toFileSpec()
    }
}
