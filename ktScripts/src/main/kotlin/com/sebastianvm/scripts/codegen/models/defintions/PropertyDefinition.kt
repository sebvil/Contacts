package com.sebastianvm.scripts.codegen.models.defintions

import kotlinx.serialization.Serializable

@Serializable
data class PropertyDefinition(
    val name: String,
    val description: String,
    val isPrimaryKey: Boolean = false,
    val schema: TypeDefinition,
)
