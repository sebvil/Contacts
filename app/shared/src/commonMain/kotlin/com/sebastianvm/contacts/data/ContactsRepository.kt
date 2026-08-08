package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact

internal interface ContactsRepository {

    suspend fun getContacts(): List<Contact>
}
