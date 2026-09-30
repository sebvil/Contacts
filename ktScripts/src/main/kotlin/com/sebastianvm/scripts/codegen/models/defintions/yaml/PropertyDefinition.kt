package com.sebastianvm.scripts.codegen.models.defintions.yaml

import kotlinx.serialization.Serializable

@Serializable
data class PropertyDefinition(
    val name: String,
    val description: String,
    val schema: TypeDefinition,
)

val PropertyDefinition.isPrimaryKey: Boolean
    get() =
        when (schema) {
            is UuidType -> schema.isPrimaryKey
            is OneToManyRelation,
            is StringType,
            is DateType -> false
        }
