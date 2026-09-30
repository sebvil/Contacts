package com.sebastianvm.contacts.repository

import com.sebastianvm.contacts.database.tables.ContactsTable
import com.sebastianvm.contacts.dto.ContactRequest
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.core.types.Some
import com.sebastianvm.core.types.ifExists
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

    suspend fun createContact(contact: ContactRequest): ContactResponse {
        return suspendTransaction(db) {
            ContactsTable.insertReturning { table ->
                    contact.id.ifExists { table[id] = it }
                    contact.name.ifExists { table[name] = it }
                }
                .map { it.toContactResponse() }
                .single()
        }
    }

    suspend fun getAllContacts(): List<ContactResponse> {
        return suspendTransaction(db) {
            ContactsTable.select(ContactsTable.columns).mapLazy { it.toContactResponse() }.toList()
        }
    }

    suspend fun getContactById(id: Uuid): ContactResponse? {
        return suspendTransaction(db) {
            ContactsTable.select(ContactsTable.columns)
                .where { ContactsTable.id eq id }
                .mapLazy { it.toContactResponse() }
                .singleOrNull()
        }
    }
}

private fun ResultRow.toContactResponse(): ContactResponse =
    ContactResponse(id = get(ContactsTable.id).value, name = Some(get(ContactsTable.name)))
