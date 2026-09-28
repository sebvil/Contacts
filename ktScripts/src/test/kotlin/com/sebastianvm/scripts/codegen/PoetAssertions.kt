package com.sebastianvm.scripts.codegen

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

fun ParameterSpec.shouldBeParameter(
    typeName: TypeName,
    parameterName: String,
    expectedDefaultValue: CodeBlock? = null,
) {
    type shouldBe typeName
    name shouldBe parameterName
    this.defaultValue shouldBe expectedDefaultValue
}

fun PropertySpec.shouldBeDataClassProperty(
    typeName: TypeName,
    propertyName: String,
) {
    mutable shouldBe false
    modifiers shouldNotContain KModifier.PRIVATE
    type shouldBe typeName
    name shouldBe propertyName
    initializer shouldBe CodeBlock.of(name)
}
