package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.DateType
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.OneToManyRelation
import com.sebastianvm.scripts.codegen.models.defintions.StringType
import com.sebastianvm.scripts.codegen.models.defintions.UuidType
import com.sebastianvm.scripts.codegen.models.defintions.className
import com.sebastianvm.scripts.codegen.models.defintions.isPrimaryKey
import com.sebastianvm.scripts.codegen.models.util.poet.fileSpecBuilder
import com.sebastianvm.scripts.codegen.models.util.poet.pluralize
import com.sebastianvm.scripts.util.EchoHandler
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec

class ExposedTableGenerator(
    private val modelDefinition: ModelDefinition,
    val foreignKeys: Map<String, String>,
) {

    private val tableName = tableName(modelDefinition.name)
    private val packageName = Constants.Packages.DATABASE_TABLES

    private val primaryKeyCount = modelDefinition.properties.count { it.isPrimaryKey }

    private val objectProperties =
        if (primaryKeyCount > 1) {
            modelDefinition.properties
        } else {
            modelDefinition.properties.filter { !it.isPrimaryKey }
        }

    context(_: EchoHandler)
    fun generateModel(): FileSpec {
        return fileSpecBuilder(
                packageName = packageName,
                fileName = tableName,
            )
            .addTable()
            .build()
    }

    private fun FileSpec.Builder.addTable(): FileSpec.Builder {
        return addType(
            TypeSpec.objectBuilder(tableName)
                .superclass()
                .addContributesBindingAnnotation()
                .addProperties()
                .addKdoc()
                .build()
        )
    }

    private fun TypeSpec.Builder.superclass(): TypeSpec.Builder {
        // Right now, only UuidTable is supported, but will likely change in the future.
        return superclass(Constants.Types.UUID_TABLE)
    }

    private fun TypeSpec.Builder.addContributesBindingAnnotation(): TypeSpec.Builder {
        return addAnnotation(
            AnnotationSpec.builder(Constants.Types.CONTRIBUTES_INTO_SET)
                .addMember("scope = %T::class", Constants.Types.APP_SCOPE)
                .addMember(
                    "binding = %T()",
                    Constants.Types.BINDING.parameterizedBy(
                        Constants.Types.ID_TABLE.parameterizedBy(STAR)
                    ),
                )
                .build()
        )
    }

    private fun TypeSpec.Builder.addProperties(): TypeSpec.Builder {
        return addProperties(
            objectProperties.mapNotNull {
                val schema = it.schema
                val columnType = Constants.Types.EXPOSED_COLUMN.parameterizedBy(schema.className)
                val initializer =
                    when (schema) {
                        is StringType -> CodeBlock.of("varchar(%S, 255)", it.name)
                        is UuidType -> {
                            val foreignKeyTable = foreignKeys[it.name]
                            if (foreignKeyTable == null) {
                                CodeBlock.of("uuid(%S)", it.name)
                            } else {
                                CodeBlock.of(
                                    "uuid(%S).references(ref = %T.id, onDelete = %T.CASCADE)",
                                    it.name,
                                    ClassName(
                                        packageName = Constants.Packages.DATABASE_TABLES,
                                        tableName(foreignKeyTable),
                                    ),
                                    Constants.Types.EXPOSED_REFERENCE_OPTION,
                                )
                            }
                        }

                        is DateType ->
                            CodeBlock.of(
                                "%M(%S)",
                                Constants.Members.EXPOSED_DATE_COLUMN,
                                it.name,
                            )

                        is OneToManyRelation -> return@mapNotNull null
                    }
                PropertySpec.builder(it.name, columnType)
                    .initializer(
                        if (it.schema.isNullable)
                            initializer.toBuilder().add(".nullable().default(null)").build()
                        else initializer
                    )
                    .addKdoc(it.description)
                    .build()
            }
        )
    }

    private fun tableName(modelName: String) = "${pluralize(modelName)}Table"

    private fun TypeSpec.Builder.addKdoc(): TypeSpec.Builder {
        return addKdoc(modelDefinition.description)
    }
}
