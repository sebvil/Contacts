package com.sebastianvm.contacts.fixtures

import com.sebastianvm.contacts.database.tables.ContactsTable
import com.sebastianvm.contacts.testUtils.TestDependencies
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.v1.core.statements.InsertStatement
import org.jetbrains.exposed.v1.r2dbc.batchInsert
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

data class ContactFixture(
    val id: Uuid,
    val name: String = "",
    val birthday: LocalDate? = null,
)

suspend fun TestDependencies.createContact(contact: ContactFixture) {
    suspendTransaction(appGraph.database()) {
        ContactsTable.insert { table ->
            table.createContact(contact = contact)
        }
    }
}

suspend fun TestDependencies.createContacts(contacts: List<ContactFixture>) {
    suspendTransaction(appGraph.database()) {
        ContactsTable.batchInsert(contacts) { contact ->
            this.createContact(contact = contact)
        }
    }
}

suspend fun TestDependencies.createContact(
    id: Uuid,
    name: String = "",
    birthday: LocalDate? = null,
) {
    createContact(ContactFixture(id = id, name = name, birthday = birthday))
}

fun InsertStatement<*>.createContact(contact: ContactFixture) {
    this[ContactsTable.id] = contact.id
    this[ContactsTable.name] = contact.name
    this[ContactsTable.birthday] = contact.birthday
}
