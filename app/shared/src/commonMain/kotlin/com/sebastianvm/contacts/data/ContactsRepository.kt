package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    /** A reactive read of locally-cached contacts. Does not trigger a network fetch. */
    fun getContacts(): Flow<List<Contact>>

    /** Fetches contacts from the network and persists them locally, if successful. */
    suspend fun refreshContacts()

    suspend fun createContact(contact: Contact)

    fun getContact(id: Uuid): Flow<Contact>
}
