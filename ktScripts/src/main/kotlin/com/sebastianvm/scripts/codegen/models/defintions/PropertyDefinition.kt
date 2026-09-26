package com.sebastianvm.scripts.codegen.models.defintions

import com.sebastianvm.scripts.codegen.models.Constants
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable sealed interface PropertyDefinition

@Serializable
@SerialName("uuid")
data class UuidProperty(val isPrimaryKey: Boolean) : PropertyDefinition

@Serializable @SerialName("string") data object StringProperty : PropertyDefinition

fun PropertyDefinition.toTypeName(): TypeName =
    when (this) {
        StringProperty -> STRING
        is UuidProperty -> Constants.Types.UUID
    }
