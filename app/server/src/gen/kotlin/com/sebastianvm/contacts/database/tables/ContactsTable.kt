package com.sebastianvm.contacts.database.tables

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.UuidTable

/** Represents information for a contact. */
object ContactsTable : UuidTable() {
    /** Name of the contact. */
    val name: Column<String> = varchar("name", 255)
}
