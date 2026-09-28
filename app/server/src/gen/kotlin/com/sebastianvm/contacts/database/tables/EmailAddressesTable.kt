package com.sebastianvm.contacts.database.tables

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.binding
import kotlin.uuid.Uuid
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.UuidTable

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
    val address: Column<String> = varchar("address", 255)

    /** Label to identify email address. */
    val label: Column<String?> = varchar("label", 255).nullable().default(null)
}
