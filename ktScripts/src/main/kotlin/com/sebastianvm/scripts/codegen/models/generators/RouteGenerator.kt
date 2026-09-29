package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.Constants
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.className
import com.sebastianvm.scripts.codegen.models.defintions.isPrimaryKey
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.DataClassPropertyDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.fileSpecBuilder
import com.sebastianvm.scripts.codegen.models.util.poet.lowercaseFirst
import com.sebastianvm.scripts.codegen.models.util.poet.pluralize
import com.sebastianvm.scripts.util.EchoHandler
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

class RouteGenerator(private val modelDefinition: ModelDefinition) {

    private val pluralModelName = pluralize(modelDefinition.name)
    private val routeName = "${pluralModelName}Route"
    private val packageName = Constants.Packages.ROUTES
    private val topLevelRouteName = ClassName(packageName, routeName)

    context(_: EchoHandler)
    fun generateModel(): FileSpec {
        return fileSpecBuilder(
                packageName = packageName,
                fileName = routeName,
            )
            .addTopLevelRoute()
            .build()
    }

    context(_: EchoHandler)
    private fun FileSpec.Builder.addTopLevelRoute(): FileSpec.Builder {
        return addType(
            TypeSpec.objectBuilder(topLevelRouteName)
                .addModifiers(KModifier.DATA)
                .addIdRoute()
                .addResourceAnnotation("/${pluralModelName.lowercaseFirst()}")
                .build()
        )
    }

    context(_: EchoHandler)
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
                                propertyName = "id",
                                type =
                                    modelDefinition.properties
                                        .first { it.isPrimaryKey }
                                        .schema
                                        .className,
                                description = "",
                            ),
                        ),
                    description = null,
                )
                .toTypeSpec {
                    this.addResourceAnnotation("{id}")
                }
        )
    }

    private fun TypeSpec.Builder.addResourceAnnotation(path: String): TypeSpec.Builder {
        return addAnnotation(
            AnnotationSpec.builder(Constants.Types.KTOR_RESOURCE).addMember("%S", path).build()
        )
    }
}
