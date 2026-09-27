package com.sebastianvm.scripts.codegen.models.validation

import com.github.ajalt.mordant.rendering.TextColors
import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.isPrimaryKey
import com.sebastianvm.scripts.util.EchoHandler
import com.sebastianvm.scripts.util.echo
import kotlin.system.exitProcess

class ModelValidator {

    var hasErrors: Boolean = false

    context(_: EchoHandler)
    fun validateModels(modelDefinitions: List<ModelDefinition>) {
        modelDefinitions.forEach { model ->
            val nullablePrimaryKeys =
                model.properties.filter { it.schema.isNullable && it.isPrimaryKey }
            validate(
                condition = nullablePrimaryKeys.isEmpty(),
                errorMessage =
                    "Model ${model.name} has nullable primary key(s): ${nullablePrimaryKeys.map { it.name }}",
            )
        }

        if (hasErrors) {
            exitProcess(1)
        }
    }

    context(_: EchoHandler)
    private fun validate(condition: Boolean, errorMessage: String) {
        if (!condition) {
            echo(TextColors.brightRed(errorMessage), err = true)
            hasErrors = true
        }
    }
}
