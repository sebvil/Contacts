package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.DateType
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.className
import com.sebastianvm.scripts.codegen.models.generators.Fixtures.makePrimaryKeyProperty
import com.sebastianvm.scripts.codegen.models.generators.Fixtures.makeProperty
import com.sebastianvm.scripts.codegen.shouldBeDataClassProperty
import com.sebastianvm.scripts.codegen.shouldBeParameter
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.TestFixture
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

val DomainModelGeneratorTest by
    generatorTestSuite(name = "DomainModelGenerator") {
        test(
            "generates single data class in the domain package",
            modelDefinition = Fixtures.makeModel(),
        ) { model, generatedObject ->
            packageName shouldBe Constants.Packages.DOMAIN_MODELS
            name shouldBe model.name
            members shouldHaveSize 1
            generatedObject.name shouldBe model.name
            generatedObject.kind shouldBe TypeSpec.Kind.CLASS
            generatedObject.modifiers shouldContain KModifier.DATA
        }

        listOf("Contact", "PhoneNumber", "EmailAddress").forEach { modelName ->
            test(
                name =
                    "generates file and data class named $modelName for model with name $modelName",
                modelDefinition = Fixtures.makeModel(name = modelName),
            ) { _, generatedObject ->
                name shouldBe modelName
                generatedObject.name shouldBe modelName
            }
        }

        test(
            "adds all properties",
            modelDefinition =
                Fixtures.makeModel(
                    properties =
                        listOf(
                            makePrimaryKeyProperty(),
                            makeProperty(),
                            makeProperty(
                                name = "date",
                                description = "Date property",
                                schema = DateType(isNullable = true),
                            ),
                        )
                ),
        ) { model, generatedObject ->
            val properties = generatedObject.propertySpecs
            properties shouldHaveSize model.properties.size

            properties.zip(model.properties).forEach { (spec, definition) ->
                spec.shouldBeDataClassProperty(
                    typeName = definition.schema.className,
                    propertyName = definition.name,
                )
            }

            val params = generatedObject.primaryConstructor!!.parameters
            params.zip(model.properties).forEach { (spec, definition) ->
                val default = if (definition.schema.isNullable) CodeBlock.of("null") else null
                spec.shouldBeParameter(
                    typeName = definition.schema.className,
                    parameterName = definition.name,
                    expectedDefaultValue = default,
                )
            }
        }

        test("documents data class", modelDefinition = Fixtures.makeModel()) {
            model,
            generatedObject ->
            val kdoc = generatedObject.kdoc.toString()
            kdoc shouldContain model.description
            model.properties.forEach { property ->
                kdoc shouldContain "@property ${property.name} ${property.description}"
            }
        }
    }

@TestRegistering
private fun TestFixture.Scope<GeneratorScopeAction>.test(
    @TestElementName name: String,
    modelDefinition: ModelDefinition,
    testConfig: TestConfig = TestConfig,
    action: FileSpec.(ModelDefinition, TypeSpec) -> Unit,
) =
    test(name, testConfig) {
        val sut = DomainModelGenerator(modelDefinition)
        with(sut.generateModel()) {
            members shouldHaveSize 1
            val generatedObject = members.first()
            generatedObject.shouldBeInstanceOf<TypeSpec>()
            action(modelDefinition, generatedObject)
        }
    }
