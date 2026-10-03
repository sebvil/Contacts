package com.sebastianvm.scripts.codegen.models.generators

val ExposedTableGeneratorTest by
    generatorTestSuite("ExposedTableGenerator") {
        test("generates exposed table objects") {
            val models = getProcessedModels()
            ExposedTableGenerator(models.first()) generatedOutputShouldBe EXPECTED_BASE_MODEL_TABLE

            ExposedTableGenerator(models[1]) generatedOutputShouldBe EXPECTED_RELATED_MODEL_TABLE
        }
    }

private const val EXPECTED_BASE_MODEL_TABLE =
    """
    package com.sebastianvm.contacts.database.tables

    import dev.zacsweers.metro.AppScope
    import dev.zacsweers.metro.ContributesIntoSet
    import dev.zacsweers.metro.binding
    import kotlin.time.Instant
    import kotlinx.datetime.LocalDate
    import org.jetbrains.exposed.v1.core.Column
    import org.jetbrains.exposed.v1.core.dao.id.IdTable
    import org.jetbrains.exposed.v1.core.dao.id.UuidTable
    import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
    import org.jetbrains.exposed.v1.datetime.date
    import org.jetbrains.exposed.v1.datetime.timestamp

    /**
     * Base model.
     */
    @ContributesIntoSet(
        scope = AppScope::class,
        binding = binding<IdTable<*>>(),
    )
    object BaseModelsTable : UuidTable() {
        /**
         * String property.
         */
        val stringProperty: Column<String> = varchar("stringProperty", 255).default("")

        /**
         * Date property.
         */
        val dateProperty: Column<LocalDate> = date("dateProperty")

        /**
         * Object creation timestamp.
         */
        val creationTimestamp: Column<Instant> =
                timestamp("creationTimestamp").defaultExpression(CurrentTimestamp)
    }

    """
        .trimIndent()

private const val EXPECTED_RELATED_MODEL_TABLE =
    """
    package com.sebastianvm.contacts.database.tables

    import dev.zacsweers.metro.AppScope
    import dev.zacsweers.metro.ContributesIntoSet
    import dev.zacsweers.metro.binding
    import kotlin.time.Instant
    import kotlin.uuid.Uuid
    import kotlinx.datetime.LocalDate
    import org.jetbrains.exposed.v1.core.Column
    import org.jetbrains.exposed.v1.core.ReferenceOption
    import org.jetbrains.exposed.v1.core.dao.id.IdTable
    import org.jetbrains.exposed.v1.core.dao.id.UuidTable
    import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
    import org.jetbrains.exposed.v1.datetime.date
    import org.jetbrains.exposed.v1.datetime.timestamp

    /**
     * Related model.
     */
    @ContributesIntoSet(
        scope = AppScope::class,
        binding = binding<IdTable<*>>(),
    )
    object RelatedModelsTable : UuidTable() {
        /**
         * Base model id.
         */
        val baseModelId: Column<Uuid> =
                uuid("baseModelId").references(ref = BaseModelsTable.id, onDelete = ReferenceOption.CASCADE)

        /**
         * Nullable string property.
         */
        val nullableStringProperty: Column<String?> =
                varchar("nullableStringProperty", 255).nullable().default(null)

        /**
         * Nullable date property.
         */
        val nullableDateProperty: Column<LocalDate?> =
                date("nullableDateProperty").nullable().default(null)

        /**
         * Object creation timestamp.
         */
        val creationTimestamp: Column<Instant> =
                timestamp("creationTimestamp").defaultExpression(CurrentTimestamp)
    }

    """
        .trimIndent()
