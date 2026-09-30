package com.sebastianvm.scripts.codegen.models.defintions.processed

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.yaml.DateType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.OneToManyRelation
import com.sebastianvm.scripts.codegen.models.defintions.yaml.PropertyDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.StringType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.UuidType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.className
import com.sebastianvm.scripts.codegen.models.defintions.yaml.isPrimaryKey
import com.sebastianvm.scripts.codegen.models.util.poet.tableName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName

sealed interface ProcessedPropertyDefinition {
    val name: String
    val domainModelType: TypeName
    val description: String
    val domainModelDefaultValue: CodeBlock?

    data class ModelProperty(
        override val name: String,
        override val domainModelType: TypeName,
        override val description: String,
        override val domainModelDefaultValue: CodeBlock?,
        val isPrimaryKey: Boolean,
        val exposedTableColumnType: TypeName,
        val exposedTableDefaultValue: CodeBlock,
    ) : ProcessedPropertyDefinition

    data class RelationshipProperty(
        override val name: String,
        override val domainModelType: TypeName,
        override val description: String,
        override val domainModelDefaultValue: CodeBlock?,
    ) : ProcessedPropertyDefinition

    companion object {
        fun from(
            propertyDefinition: PropertyDefinition,
            foreignKeys: Map<String, String>,
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
            return if (propertyDefinition.schema is OneToManyRelation) {
                RelationshipProperty(
                    name = name,
                    domainModelType = domainModelType,
                    description = description,
                    domainModelDefaultValue = domainModelDefaultValue,
                )
            } else {
                val exposedTableDefaultValue =
                    CodeBlock.builder()
                        .apply {
                            when (propertyDefinition.schema) {
                                is DateType ->
                                    add("%M(%S)", Constants.Members.EXPOSED_DATE_COLUMN, name)
                                is StringType -> add("varchar(%S, 255)", name)
                                is UuidType -> {
                                    val foreignKey = foreignKeys[name]
                                    if (foreignKey == null) {
                                        add("uuid(%S)", name)
                                    } else {
                                        add(
                                            "uuid(%S).references(ref = %T.id, onDelete = %T.CASCADE)",
                                            name,
                                            ClassName(
                                                packageName = Constants.Packages.DATABASE_TABLES,
                                                tableName(foreignKey),
                                            ),
                                            Constants.Types.EXPOSED_REFERENCE_OPTION,
                                        )
                                    }
                                }
                            }
                            if (propertyDefinition.schema.isNullable) {
                                add(".nullable().default(null)")
                            }
                        }
                        .build()
                ModelProperty(
                    name = name,
                    domainModelType = domainModelType,
                    description = description,
                    domainModelDefaultValue = domainModelDefaultValue,
                    isPrimaryKey = propertyDefinition.isPrimaryKey,
                    exposedTableColumnType =
                        Constants.Types.EXPOSED_COLUMN.parameterizedBy(domainModelType),
                    exposedTableDefaultValue = exposedTableDefaultValue,
                )
            }
        }
    }
}
