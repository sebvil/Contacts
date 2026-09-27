package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.StringType
import com.sebastianvm.scripts.codegen.models.defintions.UuidType
import com.sebastianvm.scripts.codegen.models.util.poet.fileSpecBuilder
import com.sebastianvm.scripts.codegen.models.util.poet.pluralize
import com.sebastianvm.scripts.util.EchoHandler
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

class ExposedTableGenerator(private val modelDefinition: ModelDefinition) {

    private val tableName = "${pluralize(modelDefinition.name)}Table"
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
            TypeSpec.objectBuilder(tableName).superclass().addProperties().addKdoc().build()
        )
    }

    private fun TypeSpec.Builder.superclass(): TypeSpec.Builder {
        // Right now, only UuidTable is supported, but will likely change in the future.
        return superclass(Constants.Types.UUID_TABLE)
    }

    private fun TypeSpec.Builder.addProperties(): TypeSpec.Builder {
        return addProperties(
            objectProperties.map {
                val schema = it.schema
                val columnType = Constants.Types.EXPOSED_COLUMN.parameterizedBy(schema.className)
                val initializer =
                    when (schema) {
                        StringType -> """varchar("%L", 255)"""
                        UuidType -> """uuid("%L")"""
                    }
                PropertySpec.builder(it.name, columnType)
                    .initializer(initializer, it.name)
                    .addKdoc(it.description)
                    .build()
            }
        )
    }

    private fun TypeSpec.Builder.addKdoc(): TypeSpec.Builder {
        return addKdoc(modelDefinition.description)
    }
}
