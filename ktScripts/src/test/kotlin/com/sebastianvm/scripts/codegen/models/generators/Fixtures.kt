package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.defintions.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.PropertyDefinition
import com.sebastianvm.scripts.codegen.models.defintions.StringType
import com.sebastianvm.scripts.codegen.models.defintions.TypeDefinition
import com.sebastianvm.scripts.codegen.models.defintions.UuidType

object Fixtures {

    fun makeModel(
        name: String = MODEL_NAME,
        description: String = MODEL_DESCRIPTION,
        properties: List<PropertyDefinition> =
            listOf(
                makePrimaryKeyProperty(),
                makeProperty(),
            ),
    ): ModelDefinition =
        ModelDefinition(
            name = name,
            description = description,
            properties = properties,
        )

    fun makePrimaryKeyProperty(
        name: String = PRIMARY_KEY_NAME,
        description: String = PRIMARY_KEY_DESCRIPTION,
    ): PropertyDefinition =
        PropertyDefinition(
            name = name,
            description = description,
            schema = UuidType(isPrimaryKey = true),
        )

    fun makeProperty(
        name: String = SECONDARY_PROPERTY_NAME,
        description: String = SECONDARY_PROPERTY_DESCRIPTION,
        schema: TypeDefinition = StringType,
    ): PropertyDefinition =
        PropertyDefinition(
            name = name,
            description = description,
            schema = schema,
        )

    const val MODEL_NAME = "Contact"
    const val MODEL_DESCRIPTION = "Represents information for a contact."
    const val PRIMARY_KEY_NAME = "id"
    const val PRIMARY_KEY_DESCRIPTION = "Unique identifier."

    const val SECONDARY_PROPERTY_NAME = "name"
    const val SECONDARY_PROPERTY_DESCRIPTION = "Name of the contact."
}
