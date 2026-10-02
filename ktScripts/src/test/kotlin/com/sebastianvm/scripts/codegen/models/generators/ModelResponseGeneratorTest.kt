package com.sebastianvm.scripts.codegen.models.generators

val ModelResponseGeneratorTest by
    generatorTestSuite("ModelResponeGenerator") {
        test("generates response classes") {
            val models = getProcessedModels()
            ModelResponseGenerator(models.first()) generatedOutputShouldBe
                EXPECTED_BASE_MODEL_RESPONSE

            ModelResponseGenerator(models[1]) generatedOutputShouldBe
                EXPECTED_RELATED_MODEL_RESPONSE
        }
    }

private const val EXPECTED_BASE_MODEL_RESPONSE =
    """
    package com.sebastianvm.contacts.dto

    import com.sebastianvm.core.types.None
    import com.sebastianvm.core.types.Option
    import kotlin.uuid.Uuid
    import kotlinx.datetime.LocalDate
    import kotlinx.serialization.Serializable

    /**
     * Base model.
     *
     * @property id Unique identifier.
     * @property stringProperty String property.
     * @property dateProperty Date property.
     * @property oneToManyRelationProperty One-to-many relationship property.
     */
    @Serializable
    data class BaseModelResponse(
        val id: Uuid,
        val stringProperty: Option<String> = None,
        val dateProperty: Option<LocalDate> = None,
        val oneToManyRelationProperty: Option<List<RelatedModelResponse>> = None,
    )

    """
        .trimIndent()

private const val EXPECTED_RELATED_MODEL_RESPONSE =
    """
    package com.sebastianvm.contacts.dto

    import com.sebastianvm.core.types.None
    import com.sebastianvm.core.types.Option
    import kotlin.uuid.Uuid
    import kotlinx.datetime.LocalDate
    import kotlinx.serialization.Serializable

    /**
     * Related model.
     *
     * @property id Unique identifier.
     * @property nullableStringProperty Nullable string property.
     * @property nullableDateProperty Nullable date property.
     */
    @Serializable
    data class RelatedModelResponse(
        val id: Uuid,
        val nullableStringProperty: Option<String?> = None,
        val nullableDateProperty: Option<LocalDate?> = None,
    )

    """
        .trimIndent()
