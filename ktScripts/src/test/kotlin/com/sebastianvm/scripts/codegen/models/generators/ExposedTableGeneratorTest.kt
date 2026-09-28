package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.DateType
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.StringType
import com.sebastianvm.scripts.codegen.models.defintions.UuidType
import com.sebastianvm.scripts.codegen.models.defintions.isPrimaryKey
import com.sebastianvm.scripts.codegen.models.util.poet.pluralize
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.TestFixture
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

val ExposedTableGeneratorTest by
    generatorTestSuite(name = "ExposedTableGenerator") {
        test(
            "generates single object in the database.table package",
            modelDefinition = Fixtures.makeModel(),
        ) { model ->
            val tableName = "${pluralize(model.name)}Table"
            packageName shouldBe Constants.Packages.DATABASE_TABLES
            name shouldBe tableName
            members shouldHaveSize 1
            val generatedObject = members.first()
            generatedObject.shouldBeInstanceOf<TypeSpec>()
            generatedObject.name shouldBe tableName
            generatedObject.kind shouldBe TypeSpec.Kind.OBJECT
        }

        listOf("Contact", "PhoneNumber", "EmailAddress").forEach { modelName ->
            val tableName = "${pluralize(modelName)}Table"
            test(
                name = "generates file and object named $tableName for model with name $modelName",
                modelDefinition = Fixtures.makeModel(name = modelName),
            ) {
                name shouldBe tableName
                val generatedObject = members.first() as TypeSpec
                generatedObject.name shouldBe tableName
            }
        }

        test("generates object with UuidTable superclass", modelDefinition = Fixtures.makeModel()) {
            val generatedObject = members.first() as TypeSpec
            generatedObject.superclass shouldBe Constants.Types.UUID_TABLE
        }

        test("adds only non-primary key properties", modelDefinition = Fixtures.makeModel()) { model
            ->
            val generatedObject = members.first() as TypeSpec
            val properties = generatedObject.propertySpecs
            properties shouldHaveSize model.properties.size - 1
            val nonPrimaryKeys = model.properties.filter { !it.isPrimaryKey }.map { it.name }
            properties.map { it.name } shouldContainExactly nonPrimaryKeys
        }

        listOf(
                Triple(
                    UuidType(isPrimaryKey = false),
                    Constants.Types.UUID,
                    CodeBlock.of("""uuid("prop")"""),
                ),
                Triple(StringType(), STRING, CodeBlock.of("""varchar("prop", 255)""")),
                Triple(
                    DateType(),
                    Constants.Types.DATE,
                    CodeBlock.of("""%M("prop")""", Constants.Members.EXPOSED_DATE_COLUMN),
                ),
                Triple(
                    StringType(isNullable = true),
                    STRING.copy(nullable = true) as ClassName,
                    CodeBlock.of("""varchar("prop", 255).nullable().default(null)"""),
                ),
            )
            .forEach { (typeDefinition, className, initializer) ->
                test(
                    "adds property of type Column<${className}> with initializer",
                    modelDefinition =
                        Fixtures.makeModel(
                            properties =
                                listOf(
                                    Fixtures.makePrimaryKeyProperty(),
                                    Fixtures.makeProperty(name = "prop", schema = typeDefinition),
                                )
                        ),
                ) {
                    val generatedObject = members.first() as TypeSpec
                    val prop = generatedObject.propertySpecs.first()
                    prop.type shouldBe Constants.Types.EXPOSED_COLUMN.parameterizedBy(className)
                    prop.initializer.shouldNotBeNull {
                        this shouldBe initializer
                    }
                }
            }

        test("documents object", modelDefinition = Fixtures.makeModel()) { model ->
            val generatedObject = members.first() as TypeSpec
            val kdoc = generatedObject.kdoc.toString()
            kdoc shouldBe model.description
            val objectProperties =
                model.properties.filter { !it.isPrimaryKey }.associateBy { it.name }
            generatedObject.propertySpecs.forEach {
                it.kdoc.toString() shouldBe objectProperties[it.name]!!.description
            }
        }
    }

@TestRegistering
private fun TestFixture.Scope<GeneratorScopeAction>.test(
    @TestElementName name: String,
    modelDefinition: ModelDefinition,
    testConfig: TestConfig = TestConfig,
    action: FileSpec.(ModelDefinition) -> Unit,
) =
    test(name, testConfig) {
        val sut = ExposedTableGenerator(modelDefinition)
        with(sut.generateModel()) {
            action(modelDefinition)
        }
    }
