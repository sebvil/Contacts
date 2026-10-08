package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.capitalizeFirst
import com.sebastianvm.scripts.codegen.models.util.poet.fileSpecBuilder
import com.sebastianvm.scripts.codegen.models.util.poet.lowercaseFirst
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

class RouteGenerator(private val modelDefinition: ProcessedModelDefinition) : Generator {
    private val routeName = modelDefinition.routeName
    private val packageName = Constants.Packages.ROUTES
    private val topLevelRouteName = ClassName(packageName, routeName)

    override fun generate(): FileSpec {
        return fileSpecBuilder(
                packageName = packageName,
                fileName = routeName,
            )
            .addTopLevelRoute()
            .build()
    }

    private fun FileSpec.Builder.addTopLevelRoute(): FileSpec.Builder {
        return addType(
            TypeSpec.objectBuilder(topLevelRouteName)
                .addModifiers(KModifier.DATA)
                .addIdRoute()
                .addResourceAnnotation("/${modelDefinition.pluralModelName.lowercaseFirst()}")
                .build()
        )
    }

    private fun TypeSpec.Builder.addIdRoute(): TypeSpec.Builder {
        return addType(
            DataClassDefinition(
                    packageName = packageName,
                    className = "Id",
                    properties =
                        listOf(
                            DataClassPropertyDefinition(
                                propertyName = "parent",
                                type = topLevelRouteName,
                                description = "",
                                defaultValue = CodeBlock.of("%T", topLevelRouteName),
                            ),
                            DataClassPropertyDefinition(
                                propertyName = modelDefinition.primaryKeyProperty.name,
                                type = modelDefinition.primaryKeyProperty.domainModelType,
                                description = "",
                            ),
                        ),
                    description = null,
                )
                .toTypeSpec {
                    addResourceAnnotation("{${modelDefinition.primaryKeyProperty.name}}")
                        .addChildrenResources()
                }
        )
    }

    private fun TypeSpec.Builder.addChildrenResources(): TypeSpec.Builder {
        return apply {
            modelDefinition.relationshipProperties.forEach {
                addType(
                    DataClassDefinition(
                            packageName = packageName,
                            className = it.name.capitalizeFirst(),
                            properties =
                                listOf(
                                    DataClassPropertyDefinition(
                                        propertyName = "parent",
                                        type =
                                            ClassName(packageName, modelDefinition.routeName, "Id"),
                                        description = "",
                                    )
                                ),
                            description = null,
                        )
                        .toTypeSpec {
                            this.addResourceAnnotation("/${it.name}")
                        }
                )
            }
        }
    }

    private fun TypeSpec.Builder.addResourceAnnotation(path: String): TypeSpec.Builder {
        return addAnnotation(
            AnnotationSpec.builder(Constants.Types.KTOR_RESOURCE).addMember("%S", path).build()
        )
    }
}
