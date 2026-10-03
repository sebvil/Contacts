package com.sebastianvm.contacts.database.tables

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.binding
import kotlin.time.Instant
import kotlin.uuid.Uuid
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

/** An email address. */
@ContributesIntoSet(
    scope = AppScope::class,
    binding = binding<IdTable<*>>(),
)
object EmailAddressesTable : UuidTable() {
    /** ID of the contact to whom this address belongs. */
    val contactId: Column<Uuid> =
        uuid("contactId").references(ref = ContactsTable.id, onDelete = ReferenceOption.CASCADE)

    /** Email address value. */
    val address: Column<String> = varchar("address", 255).default("")

    /** Label to identify email address. */
    val label: Column<String?> = varchar("label", 255).nullable().default(null)

    /** Object creation timestamp. */
    val creationTimestamp: Column<Instant> =
        timestamp("creationTimestamp").defaultExpression(CurrentTimestamp)
}
