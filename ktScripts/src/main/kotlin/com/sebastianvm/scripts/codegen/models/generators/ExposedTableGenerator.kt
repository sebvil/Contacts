package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.fileSpecBuilder
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec

class ExposedTableGenerator(private val modelDefinition: ProcessedModelDefinition) : Generator {

    private val tableName = modelDefinition.serverDatabaseTableName
    private val packageName = Constants.Packages.DATABASE_TABLES

    override fun generate(): FileSpec {
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
            modelDefinition.modelProperties.map {
                val columnType = it.exposedTableColumnType
                val initializer = it.exposedTableDefaultValue
                PropertySpec.builder(it.name, columnType)
                    .initializer(initializer)
                    .addKdoc(it.description)
                    .build()
            }
        )
    }

    private fun TypeSpec.Builder.addKdoc(): TypeSpec.Builder {
        return addKdoc(modelDefinition.description)
    }
}
