package com.sebastianvm.scripts.codegen.models.validation

import com.github.ajalt.mordant.rendering.TextColors
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.OneToManyRelation
import com.sebastianvm.scripts.codegen.models.defintions.UuidType
import com.sebastianvm.scripts.codegen.models.defintions.isPrimaryKey
import com.sebastianvm.scripts.util.EchoHandler
import com.sebastianvm.scripts.util.echo
import kotlin.system.exitProcess

class ModelValidator {

    var hasErrors: Boolean = false

    context(_: EchoHandler)
    fun validateModels(modelDefinitions: List<ModelDefinition>) {
        validatePrimaryKeys(modelDefinitions)
        validateOneToManyRelationships(modelDefinitions)
        if (hasErrors) {
            exitProcess(1)
        }
    }

    context(_: EchoHandler)
    private fun validatePrimaryKeys(modelDefinitions: List<ModelDefinition>) {
        modelDefinitions.forEach { model ->
            val hasPrimaryKey = model.properties.any { it.isPrimaryKey }
            validate(hasPrimaryKey, "Model ${model.name} does not have a primary key.")

            val nullablePrimaryKeys =
                model.properties.filter { it.schema.isNullable && it.isPrimaryKey }
            validate(
                condition = nullablePrimaryKeys.isEmpty(),
                errorMessage =
                    "Model ${model.name} has nullable primary key(s): ${nullablePrimaryKeys.map { it.name }}",
            )
        }
    }

    context(_: EchoHandler)
    private fun validateOneToManyRelationships(modelDefinitions: List<ModelDefinition>) {
        val definitionsMap = modelDefinitions.associateBy { it.name }
        modelDefinitions.forEach { model ->
            val relationships =
                model.properties.map { it.schema }.filterIsInstance<OneToManyRelation>()
            relationships.forEach { relationship ->
                val relationshipModel = relationship.model
                val relatedModel = definitionsMap[relationshipModel]
                validate(
                    condition = relatedModel != null,
                    errorMessage =
                        "Model $relationshipModel referenced in ${model.name} does not exist",
                )
                relatedModel ?: return@forEach
                val referencePropertyName = "${model.name.replaceFirstChar { it.lowercase() }}Id"
                val referenceIdProperty =
                    relatedModel.properties.find { prop -> prop.name == referencePropertyName }
                validate(
                    condition = referenceIdProperty != null,
                    errorMessage =
                        "Model $relationshipModel must have a property named $referencePropertyName to " +
                            "satisfy one-to-many relationship with ${model.name}",
                )
                referenceIdProperty ?: return@forEach
                validate(
                    referenceIdProperty.schema is UuidType,
                    errorMessage =
                        "Property $referencePropertyName in $relationshipModel must be of type UUID",
                )
            }
        }
    }

    context(_: EchoHandler)
    private fun validate(condition: Boolean, errorMessage: String) {
        if (!condition) {
            echo(TextColors.brightRed(errorMessage), err = true)
            hasErrors = true
        }
    }
}
