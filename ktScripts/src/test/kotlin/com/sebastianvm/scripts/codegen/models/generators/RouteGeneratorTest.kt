package com.sebastianvm.scripts.codegen.models.generators

val RouteGeneratorTest by
    generatorTestSuite("RouteGenerator") {
        test("generates exposed resource classes") {
            val models = getProcessedModels()
            RouteGenerator(models.first()) generatedOutputShouldBe EXPECTED_BASE_MODEL_RESOURCE

            RouteGenerator(models[1]) generatedOutputShouldBe EXPECTED_RELATED_MODEL_RESOURCE
        }
    }

private const val EXPECTED_BASE_MODEL_RESOURCE =
    """
    package com.sebastianvm.contacts.routes

    import io.ktor.resources.Resource
    import kotlin.uuid.Uuid

    @Resource("/baseModels")
    data object BaseModelsRoute {
        @Resource("{id}")
        data class Id(
            val parent: BaseModelsRoute = BaseModelsRoute,
            val id: Uuid,
        ) {
            @Resource("/oneToManyRelationProperty")
            data class OneToManyRelationProperty(
                val parent: Id,
            )
        }
    }

    """
        .trimIndent()

private const val EXPECTED_RELATED_MODEL_RESOURCE =
    """
    package com.sebastianvm.contacts.routes

    import io.ktor.resources.Resource
    import kotlin.uuid.Uuid

    @Resource("/relatedModels")
    data object RelatedModelsRoute {
        @Resource("{id}")
        data class Id(
            val parent: RelatedModelsRoute = RelatedModelsRoute,
            val id: Uuid,
        )
    }

    """
        .trimIndent()
