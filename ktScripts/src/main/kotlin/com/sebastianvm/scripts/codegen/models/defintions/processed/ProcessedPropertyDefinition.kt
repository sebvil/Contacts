package com.sebastianvm.scripts.codegen.models.defintions.processed

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.yaml.DateType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.OneToManyRelation
import com.sebastianvm.scripts.codegen.models.defintions.yaml.PropertyDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.StringType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.TypeDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.UuidType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.className
import com.sebastianvm.scripts.codegen.models.defintions.yaml.isPrimaryKey
import com.sebastianvm.scripts.codegen.models.util.poet.tableName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName

data class ProcessedPropertyDefinition(
    val name: String,
    val domainModelType: TypeName,
    val requestType: TypeName,
    val responseType: TypeName,
    val description: String,
    val domainModelDefaultValue: CodeBlock?,
    val isForeignKey: Boolean,
    val exposedTableColumnType: TypeName,
    val exposedTableDefaultValue: CodeBlock?,
    val resultRowMapperInitializer: CodeBlock,
    val responseDefaultValue: CodeBlock?,
) {
    companion object {
        fun from(
            propertyDefinition: PropertyDefinition,
            foreignKeys: Map<String, String>,
            tableName: ClassName,
        ): ProcessedPropertyDefinition {
            val name = propertyDefinition.name
            val domainModelType = propertyDefinition.schema.className
            val description = propertyDefinition.description
            val domainModelDefaultValue =
                when {
                    propertyDefinition.schema.isNullable -> CodeBlock.of("null")
                    propertyDefinition.schema is OneToManyRelation -> CodeBlock.of("emptyList()")
                    else -> null
                }
            val schema = propertyDefinition.schema
            val requestType =
                if (schema is OneToManyRelation) {
                    getToManyRelationModelDtoType(
                        modelName = propertyDefinition.schema.model,
                        suffix = "Request",
                    )
                } else {
                    Constants.Types.OPTION.parameterizedBy(domainModelType)
                }
            val responseType =
                when {
                    propertyDefinition.isPrimaryKey -> domainModelType
                    schema is OneToManyRelation ->
                        getToManyRelationModelDtoType(
                            modelName = propertyDefinition.schema.model,
                            suffix = "Response",
                        )

                    else -> Constants.Types.OPTION.parameterizedBy(domainModelType)
                }
            val foreignKey = foreignKeys[name]

            return ProcessedPropertyDefinition(
                name = name,
                domainModelType = domainModelType,
                requestType = requestType,
                responseType = responseType,
                description = description,
                domainModelDefaultValue = domainModelDefaultValue,
                isForeignKey = foreignKey != null,
                exposedTableColumnType =
                    Constants.Types.EXPOSED_COLUMN.parameterizedBy(domainModelType),
                exposedTableDefaultValue =
                    getExposedTableDefaultValue(
                        schema = schema,
                        propertyName = name,
                        foreignKey = foreignKey,
                    ),
                responseDefaultValue =
                    if (propertyDefinition.isPrimaryKey) null
                    else
                        CodeBlock.of(
                            "%T",
                            Constants.Types.NONE,
                        ),
                resultRowMapperInitializer =
                    getResultRowToResponseValue(
                        tableName = tableName,
                        propertyName = name,
                        schema = schema,
                        isPrimaryKey = propertyDefinition.isPrimaryKey,
                    ),
            )
        }

        private fun getToManyRelationModelDtoType(modelName: String, suffix: String): TypeName {
            return Constants.Types.OPTION.parameterizedBy(
                LIST.parameterizedBy(
                    ClassName(
                        Constants.Packages.DTO,
                        "$modelName$suffix",
                    )
                )
            )
        }

        private fun getExposedTableDefaultValue(
            schema: TypeDefinition,
            propertyName: String,
            foreignKey: String?,
        ): CodeBlock? {
            if (schema is OneToManyRelation) return null
            return CodeBlock.builder()
                .apply {
                    when (schema) {
                        is DateType ->
                            add("%M(%S)", Constants.Members.EXPOSED_DATE_COLUMN, propertyName)

                        is StringType -> add("varchar(%S, 255)", propertyName)
                        is UuidType -> {
                            if (foreignKey == null) {
                                add("uuid(%S)", propertyName)
                            } else {
                                add(
                                    "uuid(%S).references(ref = %T.id, onDelete = %T.CASCADE)",
                                    propertyName,
                                    ClassName(
                                        packageName = Constants.Packages.DATABASE_TABLES,
                                        tableName(foreignKey),
                                    ),
                                    Constants.Types.EXPOSED_REFERENCE_OPTION,
                                )
                            }
                        }
                    }
                    if (schema.isNullable) {
                        add(".nullable().default(null)")
                    }
                }
                .build()
        }

        private fun getResultRowToResponseValue(
            tableName: ClassName,
            propertyName: String,
            schema: TypeDefinition,
            isPrimaryKey: Boolean,
        ): CodeBlock {
            return when (schema) {
                is UuidType if isPrimaryKey ->
                    CodeBlock.of(
                        "%L = get(%T.%L).value,",
                        propertyName,
                        tableName,
                        propertyName,
                    )

                is UuidType,
                is StringType,
                is DateType ->
                    CodeBlock.of(
                        "%L = %T(get(%T.%L)),",
                        propertyName,
                        Constants.Types.SOME,
                        tableName,
                        propertyName,
                    )

                is OneToManyRelation -> CodeBlock.of("%L = %L,", propertyName, propertyName)
            }
        }
    }
}
