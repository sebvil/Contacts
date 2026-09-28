package com.sebastianvm.scripts.codegen.models.defintions

import com.sebastianvm.scripts.codegen.models.Constants
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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

@Serializable
@SerialName("oneToManyRelation")
data class OneToManyRelation(val model: String) : TypeDefinition {
    @Transient override val isNullable: Boolean = false
}

val TypeDefinition.className: TypeName
    get() =
        when (this) {
            is DateType -> Constants.Types.DATE
            is StringType -> STRING
            is UuidType -> Constants.Types.UUID
            is OneToManyRelation ->
                LIST.parameterizedBy(ClassName(Constants.Packages.DOMAIN_MODELS, model))
        }.copy(nullable = isNullable)
