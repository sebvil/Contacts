package com.sebastianvm.scripts.codegen.models.util.poet

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

data class DataClassDefinition(
    val packageName: String,
    val className: String,
    val properties: List<DataClassPropertyDefinition>,
    val description: String,
) {

    fun toFileSpec(): FileSpec {
        return fileSpecBuilder(packageName, className).addType(toTypeSpec()).build()
    }

    private fun toTypeSpec(): TypeSpec {
        return TypeSpec.classBuilder(ClassName(packageName, className))
            .addModifiers(KModifier.DATA)
            .addPrimaryConstructor()
            .addProperties(
                propertySpecs =
                    properties.map {
                        PropertySpec.builder(name = it.propertyName, type = it.type)
                            .initializer(it.propertyName)
                            .build()
                    }
            )
            .build()
    }

    private fun TypeSpec.Builder.addPrimaryConstructor(): TypeSpec.Builder {
        val constructorSpec =
            FunSpec.constructorBuilder()
                .addParameters(
                    properties.map {
                        ParameterSpec.builder(name = it.propertyName, type = it.type).build()
                    }
                )
                .build()
        return this.primaryConstructor(constructorSpec)
    }
}
