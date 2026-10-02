package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.squareup.kotlinpoet.FileSpec

class DomainModelGenerator(private val modelDefinition: ProcessedModelDefinition) : Generator {

    override fun generate(): FileSpec {
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
