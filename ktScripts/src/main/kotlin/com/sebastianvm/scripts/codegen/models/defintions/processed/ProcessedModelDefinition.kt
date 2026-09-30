package com.sebastianvm.scripts.codegen.models.defintions.processed

import com.sebastianvm.scripts.codegen.models.defintions.yaml.ModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.pluralize

data class ProcessedModelDefinition(
    val domainModelName: String,
    val serverDatabaseTableName: String,
    val routeName: String,
    val pluralModelName: String,
    val description: String,
    val primaryKeyProperty: ProcessedPropertyDefinition.ModelProperty,
    val modelProperties: List<ProcessedPropertyDefinition.ModelProperty>,
    val relationshipProperties: List<ProcessedPropertyDefinition.RelationshipProperty>,
) {

    val allProperties: List<ProcessedPropertyDefinition> =
        listOf(primaryKeyProperty) + modelProperties + relationshipProperties

    companion object {
        fun from(
            modelDefinition: ModelDefinition,
            foreignKeys: Map<String, String>,
        ): ProcessedModelDefinition {
            val properties =
                modelDefinition.properties.map {
                    ProcessedPropertyDefinition.from(
                        propertyDefinition = it,
                        foreignKeys = foreignKeys,
                    )
                }
            val modelProperties =
                properties.filterIsInstance<ProcessedPropertyDefinition.ModelProperty>()
            val pluralModelName: String = pluralize(modelDefinition.name)
            return ProcessedModelDefinition(
                domainModelName = modelDefinition.name,
                serverDatabaseTableName = "${pluralModelName}Table",
                routeName = "${pluralModelName}Route",
                pluralModelName = pluralModelName,
                description = modelDefinition.description,
                primaryKeyProperty = modelProperties.first { it.isPrimaryKey },
                modelProperties = modelProperties.filter { !it.isPrimaryKey },
                relationshipProperties =
                    properties.filterIsInstance<ProcessedPropertyDefinition.RelationshipProperty>(),
            )
        }
    }
}
