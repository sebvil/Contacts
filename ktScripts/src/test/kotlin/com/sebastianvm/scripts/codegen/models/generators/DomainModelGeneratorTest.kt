package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.StringProperty
import com.sebastianvm.scripts.codegen.models.defintions.UuidProperty
import com.sebastianvm.scripts.codegen.shouldBeDataClassProperty
import com.sebastianvm.scripts.codegen.shouldBeParameter
import com.sebastianvm.scripts.util.EchoHandler
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

val DomainModelGeneratorTest by testSuite {
    test("generates simple model") {
        val modelDefinition =
            ModelDefinition(
                name = "Contact",
                properties =
                    mapOf(
                        "id" to UuidProperty(isPrimaryKey = true, description = ""),
                        "name" to StringProperty(description = ""),
                    ),
                description = "",
            )

        val sut = DomainModelGenerator(modelDefinition)

        val generatedModel = with(object : EchoHandler {}) { sut.generateModel() }
        with(generatedModel) {
            packageName shouldBe Constants.Packages.DOMAIN_MODELS
            name shouldBe modelDefinition.name
            members shouldHaveSize 1
            val generatedClass = members.first()
            generatedClass.shouldBeInstanceOf<TypeSpec>()
            generatedClass.name shouldBe modelDefinition.name
            generatedClass.modifiers shouldContain KModifier.DATA

            // Constructor
            val primaryConstructor = generatedClass.primaryConstructor
            primaryConstructor.shouldNotBeNull()
            val parameters = primaryConstructor.parameters
            parameters shouldHaveSize 2
            parameters[0].shouldBeParameter(typeName = Constants.Types.UUID, parameterName = "id")
            parameters[1].shouldBeParameter(typeName = STRING, parameterName = "name")

            val properties = generatedClass.propertySpecs
            properties shouldHaveSize 2
            properties[0].shouldBeDataClassProperty(
                typeName = Constants.Types.UUID,
                propertyName = "id",
            )
            properties[1].shouldBeDataClassProperty(typeName = STRING, propertyName = "name")
        }
    }
}
