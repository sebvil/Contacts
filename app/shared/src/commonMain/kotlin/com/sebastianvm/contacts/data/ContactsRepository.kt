package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    /** A reactive read of locally-cached contacts. Does not trigger a network fetch. */
    fun getContacts(): Flow<List<Contact>>

    /** Fetches contacts from the network and persists them locally, if successful. */
    suspend fun refreshContacts()
}
