package com.sebastianvm.scripts.codegen.models.defintions

import com.sebastianvm.scripts.codegen.models.Constants
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface TypeDefinition {

    val className: TypeName
}

@Serializable
@SerialName("uuid")
data object UuidType : TypeDefinition {
    override val className: TypeName = Constants.Types.UUID
}

@Serializable
@SerialName("string")
data object StringType : TypeDefinition {
    override val className: TypeName = STRING
}
