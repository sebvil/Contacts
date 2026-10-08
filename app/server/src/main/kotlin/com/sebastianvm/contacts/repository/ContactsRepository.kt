package com.sebastianvm.contacts.repository

import com.sebastianvm.contacts.database.tables.ContactsTable
import com.sebastianvm.contacts.database.tables.EmailAddressesTable
import com.sebastianvm.contacts.dto.ContactRequest
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.core.types.None
import com.sebastianvm.core.types.Some
import com.sebastianvm.core.types.ifExists
import com.sebastianvm.core.types.map
import dev.zacsweers.metro.Inject
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.batchInsert
import org.jetbrains.exposed.v1.r2dbc.insertReturning
import org.jetbrains.exposed.v1.r2dbc.mapLazy
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

@Inject
class ContactsRepository(private val db: R2dbcDatabase) {

    suspend fun createContact(contact: ContactRequest): ContactResponse {
        return suspendTransaction(db) {
            val result =
                ContactsTable.insertReturning { table ->
                        contact.id.ifExists { table[id] = it }
                        contact.name.ifExists { table[name] = it }
                        contact.birthday.ifExists { table[birthday] = it }
                    }
                    .single()

            val emailAddresses =
                contact.emailAddresses.map { emailAddresses ->
                    EmailAddressesTable.batchInsert(emailAddresses) {
                            this[EmailAddressesTable.contactId] = result[ContactsTable.id].value
                            it.id.ifExists { id -> this[EmailAddressesTable.id] = id }
                            it.address.ifExists { address ->
                                this[EmailAddressesTable.address] = address
                            }
                            it.label.ifExists { label -> this[EmailAddressesTable.label] = label }
                        }
                        .map { it.toEmailAddressResponse() }
                }
            result.toContactResponse(emailAddresses)
        }
    }

    suspend fun getAllContacts(): List<ContactResponse> {
        return suspendTransaction(db) {
            val emailAddresses =
                EmailAddressesTable.selectAll()
                    .mapLazy { it[EmailAddressesTable.contactId] to it.toEmailAddressResponse() }
                    .toList()
                    .groupBy(keySelector = { it.first }, valueTransform = { it.second })
            ContactsTable.select(ContactsTable.columns)
                .mapLazy { resultRow ->
                    val contactId = resultRow[ContactsTable.id].value
                    resultRow.toContactResponse(
                        emailAddresses = emailAddresses[contactId]?.let { Some(it) } ?: None
                    )
                }
                .toList()
        }
    }

    suspend fun getContactById(id: Uuid): ContactResponse? {
        return suspendTransaction(db) {
            val emailAddresses =
                EmailAddressesTable.selectAll()
                    .where { EmailAddressesTable.contactId eq id }
                    .mapLazy { it.toEmailAddressResponse() }
                    .toList()
            ContactsTable.selectAll()
                .where { ContactsTable.id eq id }
                .mapLazy { it.toContactResponse(emailAddresses = Some(emailAddresses)) }
                .singleOrNull()
        }
    }
}
