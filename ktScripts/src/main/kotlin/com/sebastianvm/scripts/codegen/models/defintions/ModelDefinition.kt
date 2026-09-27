package com.sebastianvm.scripts.codegen.models.defintions

import kotlinx.serialization.Serializable

@Serializable
data class ModelDefinition(
    val name: String,
    val properties: List<PropertyDefinition>,
    val description: String,
)
