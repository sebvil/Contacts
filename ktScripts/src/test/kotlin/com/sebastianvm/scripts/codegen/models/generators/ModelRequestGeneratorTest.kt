package com.sebastianvm.scripts.codegen.models.generators

val ModelRequestGeneratorTest by
    generatorTestSuite("ModelRequestGenerator") {
        test("generates request classes") {
            val models = getProcessedModels()
            ModelRequestGenerator(models.first()) generatedOutputShouldBe
                EXPECTED_BASE_MODEL_REQUEST

            ModelRequestGenerator(models[1]) generatedOutputShouldBe EXPECTED_RELATED_MODEL_REQUEST
        }
    }

const val EXPECTED_BASE_MODEL_REQUEST =
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
    data class BaseModelRequest(
        val id: Option<Uuid> = None,
        val stringProperty: Option<String> = None,
        val dateProperty: Option<LocalDate> = None,
        val oneToManyRelationProperty: Option<List<RelatedModelRequest>> = None,
    )

    """
        .trimIndent()

const val EXPECTED_RELATED_MODEL_REQUEST =
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
    data class RelatedModelRequest(
        val id: Option<Uuid> = None,
        val nullableStringProperty: Option<String?> = None,
        val nullableDateProperty: Option<LocalDate?> = None,
    )

    """
        .trimIndent()
