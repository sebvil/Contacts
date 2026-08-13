@file:Suppress("RETURN_VALUE_NOT_USED_COERCION")

package com.sebastianvm.contacts.app.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.sebastianvm.contacts.domain.Contact
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
public class SqlDelightLocalContactsDataSource(
    private val contactsQueries: ContactsQueries,
    private val ioDispatcher: CoroutineDispatcher,
) : LocalContactsDataSource {

    override fun getAllContacts(): Flow<List<Contact>> {
        return contactsQueries.selectAll().asFlow().mapToList(ioDispatcher).map { contacts ->
            contacts.map { contact ->
                contact.toDomainContact()
            }
        }
    }

    override suspend fun insertContacts(contacts: List<Contact>) {
        contactsQueries.transaction {
            contacts.forEach {
                contactsQueries.insertContact(it.id, it.name)
            }
        }
    }
}

private fun com.sebastianvm.contacts.app.database.Contact.toDomainContact(): Contact {
    return Contact(
        id = id,
        name = name,
    )
}
