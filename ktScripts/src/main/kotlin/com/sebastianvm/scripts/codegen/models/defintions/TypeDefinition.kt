package com.sebastianvm.scripts.codegen.models.defintions

import com.sebastianvm.scripts.codegen.models.Constants
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface TypeDefinition {

    val isNullable: Boolean
}

@Serializable
@SerialName("uuid")
data class UuidType(val isPrimaryKey: Boolean = false, override val isNullable: Boolean = false) :
    TypeDefinition

@Serializable
@SerialName("string")
data class StringType(override val isNullable: Boolean = false) : TypeDefinition

@Serializable
@SerialName("date")
data class DateType(override val isNullable: Boolean = false) : TypeDefinition

val TypeDefinition.className: TypeName
    get() =
        when (this) {
            is DateType -> Constants.Types.DATE
            is StringType -> STRING
            is UuidType -> Constants.Types.UUID
        }.copy(nullable = isNullable)
