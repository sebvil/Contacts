package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    fun getContacts(): Flow<List<Contact>>
}
