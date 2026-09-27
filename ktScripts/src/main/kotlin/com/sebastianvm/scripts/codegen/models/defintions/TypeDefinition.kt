package com.sebastianvm.scripts.codegen.models.defintions

import com.sebastianvm.scripts.codegen.models.Constants
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed interface TypeDefinition {

    val className: TypeName
}

@Serializable
@SerialName("uuid")
data class UuidType(val isPrimaryKey: Boolean = false) : TypeDefinition {
    @Transient override val className: TypeName = Constants.Types.UUID
}

@Serializable
@SerialName("string")
data object StringType : TypeDefinition {
    override val className: TypeName = STRING
}
