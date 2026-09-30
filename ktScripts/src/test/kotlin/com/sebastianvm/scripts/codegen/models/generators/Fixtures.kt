package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.codegen.models.defintions.yaml.DateType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.ModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.PropertyDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.StringType
import com.sebastianvm.scripts.codegen.models.defintions.yaml.TypeDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.UuidType
import org.intellij.lang.annotations.Language

object Fixtures {

    fun makeModel(
        name: String = MODEL_NAME,
        description: String = MODEL_DESCRIPTION,
        properties: List<PropertyDefinition> =
            listOf(
                makePrimaryKeyProperty(),
                makeProperty(),
                makeProperty(
                    name = "date",
                    description = "Date property",
                    schema = DateType(isNullable = true),
                ),
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
        schema: TypeDefinition = StringType(),
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

    @Language("yaml")
    const val BASE_MODEL_YAML =
        """
        |name: BaseModel
        |description: Base model.
        |properties:
        |  - name: id
        |    description: Unique identifier.
        |    schema:
        |      type: uuid
        |      isPrimaryKey: true
        |  - name: stringProperty
        |    description: String property.
        |    schema:
        |      type: string
        |  - name: dateProperty
        |    description: Date property.
        |    schema:
        |      type: date
        |  - name: oneToManyRelationProperty
        |    description: One-to-many relationship property.
        |    schema:
        |      type: oneToManyRelation
        |      model: RelatedModel
        """
            .trimMargin()

    @Language("yaml")
    const val RELATED_MODEL =
        """
        |name: RelatedModel
        |description: Related model.
        |properties:
        |  - name: id
        |    description: Unique identifier.
        |    schema:
        |      type: uuid
        |      isPrimaryKey: true
        |  - name: baseModelId
        |    description: Base model id.
        |    schema:
        |      type: uuid
        |  - name: nullableStringProperty
        |    description: Nullable string property.
        |    schema:
        |      type: string
        |      isNullable: true
        |  - name: nullableDateProperty
        |    description: Nullable date property.
        |    schema:
        |      type: date
        |      isNullable: true
        """
            .trimMargin()
}
