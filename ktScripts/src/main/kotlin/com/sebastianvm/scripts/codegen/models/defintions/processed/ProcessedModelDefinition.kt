package com.sebastianvm.scripts.codegen.models.defintions.processed

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.yaml.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.OneToManyRelation
import com.sebastianvm.scripts.codegen.models.defintions.yaml.isPrimaryKey
import com.sebastianvm.scripts.codegen.models.util.poet.pluralize
import com.squareup.kotlinpoet.ClassName

data class ProcessedModelDefinition(
    val domainModelName: String,
    val serverDatabaseTableName: String,
    val routeName: String,
    val requestName: String,
    val responseName: String,
    val repositoryName: String,
    val pluralModelName: String,
    val description: String,
    val primaryKeyProperty: ProcessedPropertyDefinition,
    val modelProperties: List<ProcessedPropertyDefinition>,
    val relationshipProperties: List<ProcessedPropertyDefinition>,
) {

    val allProperties: List<ProcessedPropertyDefinition> =
        listOf(primaryKeyProperty) + modelProperties + relationshipProperties

    companion object {
        fun from(
            modelDefinition: ModelDefinition,
            foreignKeys: Map<String, String>,
        ): ProcessedModelDefinition {
            val pluralModelName: String = pluralize(modelDefinition.name)
            val serverDatabaseTableName = "${pluralModelName}Table"
            val tableName = ClassName(Constants.Packages.DATABASE_TABLES, serverDatabaseTableName)
            val primaryKeyProperty =
                ProcessedPropertyDefinition.from(
                    tableName = tableName,
                    propertyDefinition = modelDefinition.properties.first { it.isPrimaryKey },
                    foreignKeys = emptyMap(),
                )
            val modelProperties =
                modelDefinition.properties
                    .filter { !it.isPrimaryKey && it.schema !is OneToManyRelation }
                    .map {
                        ProcessedPropertyDefinition.from(
                            tableName = tableName,
                            propertyDefinition = it,
                            foreignKeys = foreignKeys,
                        )
                    }
            val relationshipProperties =
                modelDefinition.properties
                    .filter { it.schema is OneToManyRelation }
                    .map {
                        ProcessedPropertyDefinition.from(
                            tableName = tableName,
                            propertyDefinition = it,
                            foreignKeys = foreignKeys,
                        )
                    }
            return ProcessedModelDefinition(
                domainModelName = modelDefinition.name,
                serverDatabaseTableName = "${pluralModelName}Table",
                routeName = "${pluralModelName}Route",
                requestName = "${modelDefinition.name}Request",
                responseName = "${modelDefinition.name}Response",
                repositoryName = "${pluralModelName}Repository",
                pluralModelName = pluralModelName,
                description = modelDefinition.description,
                primaryKeyProperty = primaryKeyProperty,
                modelProperties = modelProperties,
                relationshipProperties = relationshipProperties,
            )
        }
    }
}

val ProcessedModelDefinition.responseClassName: ClassName
    get() = ClassName(Constants.Packages.DTO, responseName)
