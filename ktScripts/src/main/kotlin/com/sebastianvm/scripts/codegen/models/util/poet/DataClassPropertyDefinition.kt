package com.sebastianvm.scripts.codegen.models.util.poet

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

data class DataClassPropertyDefinition(
    val propertyName: String,
    val type: TypeName,
    val description: String,
    val defaultValue: CodeBlock? = null,
)
