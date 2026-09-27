package com.sebastianvm.contacts.database.tables

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.date

/** Represents information for a contact. */
object ContactsTable : UuidTable() {
    /** Name of the contact. */
    val name: Column<String> = varchar("name", 255)

    /** Contact's birthday. */
    val birthday: Column<LocalDate?> = date("birthday").nullable().default(null)
}
