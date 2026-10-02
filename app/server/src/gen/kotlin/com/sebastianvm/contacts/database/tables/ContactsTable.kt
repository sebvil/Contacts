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

/** Represents information for a contact. */
@ContributesIntoSet(
    scope = AppScope::class,
    binding = binding<IdTable<*>>(),
)
object ContactsTable : UuidTable() {
    /** Name of the contact. */
    val name: Column<String> = varchar("name", 255)

    /** Contact's birthday. */
    val birthday: Column<LocalDate?> = date("birthday").nullable().default(null)

    /** Object creation timestamp. */
    val creationTimestamp: Column<Instant> =
        timestamp("creationTimestamp").defaultExpression(CurrentTimestamp)
}
