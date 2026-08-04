package com.sebastianvm.contacts.repository

import com.sebastianvm.contacts.database.tables.ContactsTable
import com.sebastianvm.contacts.domain.Contact
import dev.zacsweers.metro.Inject
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.insertReturning
import org.jetbrains.exposed.v1.r2dbc.mapLazy
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

@Inject
class ContactsRepository(private val db: R2dbcDatabase) {

    suspend fun createContact(contact: Contact): Contact {
        return suspendTransaction(db) {
            ContactsTable.insertReturning {
                    it[id] = contact.id
                    it[name] = contact.name
                }
                .map { it.toContact() }
                .single()
        }
    }

    suspend fun getAllContacts(): List<Contact> {
        return suspendTransaction(db) {
            ContactsTable.select(ContactsTable.columns).mapLazy { it.toContact() }.toList()
        }
    }

    suspend fun getContactById(id: Uuid): Contact? {
        return suspendTransaction(db) {
            ContactsTable.select(ContactsTable.columns)
                .where { ContactsTable.id eq id }
                .mapLazy { it.toContact() }
                .singleOrNull()
        }
    }
}

private fun ResultRow.toContact(): Contact =
    Contact(id = get(ContactsTable.id).value, name = get(ContactsTable.name))
