package com.sebastianvm.scripts.codegen.models.generators

val ServerRepositoryGeneratorTest by
    generatorTestSuite("ServerRepositoryGenerator") {
        test("generates repository class and mapper method") {
            val models = getProcessedModels()
            ServerRepositoryGenerator(models.first()) generatedOutputShouldBe
                EXPECTED_BASE_MODEL_REPOSITORY

            ServerRepositoryGenerator(models[1]) generatedOutputShouldBe
                EXPECTED_RELATED_MODEL_REPOSITORY
        }
    }

private const val EXPECTED_BASE_MODEL_REPOSITORY =
    """
    package com.sebastianvm.contacts.repository

    import com.sebastianvm.contacts.database.tables.BaseModelsTable
    import com.sebastianvm.contacts.dto.BaseModelResponse
    import com.sebastianvm.contacts.dto.RelatedModelResponse
    import com.sebastianvm.core.types.Option
    import com.sebastianvm.core.types.Some
    import org.jetbrains.exposed.v1.core.ResultRow

    fun ResultRow.toBaseModelResponse(oneToManyRelationProperty: Option<List<RelatedModelResponse>>): BaseModelResponse {
        return BaseModelResponse(
            id = get(BaseModelsTable.id).value,
            stringProperty = Some(get(BaseModelsTable.stringProperty)),
            dateProperty = Some(get(BaseModelsTable.dateProperty)),
            oneToManyRelationProperty = oneToManyRelationProperty,
        )
    }

    """
        .trimIndent()

private const val EXPECTED_RELATED_MODEL_REPOSITORY =
    """
    package com.sebastianvm.contacts.repository

    import com.sebastianvm.contacts.database.tables.RelatedModelsTable
    import com.sebastianvm.contacts.dto.RelatedModelResponse
    import com.sebastianvm.core.types.Some
    import org.jetbrains.exposed.v1.core.ResultRow

    fun ResultRow.toRelatedModelResponse(): RelatedModelResponse {
        return RelatedModelResponse(
            id = get(RelatedModelsTable.id).value,
            nullableStringProperty = Some(get(RelatedModelsTable.nullableStringProperty)),
            nullableDateProperty = Some(get(RelatedModelsTable.nullableDateProperty)),
        )
    }

    """
        .trimIndent()
