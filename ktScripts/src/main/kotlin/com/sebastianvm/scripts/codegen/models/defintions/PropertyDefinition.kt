package com.sebastianvm.scripts.codegen.models.defintions

import com.sebastianvm.scripts.codegen.models.Constants
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PropertyDefinition {
    val description: String
}

@Serializable
@SerialName("uuid")
data class UuidProperty(val isPrimaryKey: Boolean, override val description: String) :
    PropertyDefinition

@Serializable
@SerialName("string")
data class StringProperty(override val description: String) : PropertyDefinition

fun PropertyDefinition.toTypeName(): TypeName =
    when (this) {
        is StringProperty -> STRING
        is UuidProperty -> Constants.Types.UUID
    }
