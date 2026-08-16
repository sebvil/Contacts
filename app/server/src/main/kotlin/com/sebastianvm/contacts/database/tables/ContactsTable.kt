package com.sebastianvm.contacts.database.tables

import org.jetbrains.exposed.v1.core.dao.id.UuidTable

object ContactsTable : UuidTable() {
    val name = varchar("name", 255)
}
