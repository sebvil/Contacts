package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.app.database.LocalContactsDataSource
import com.sebastianvm.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocalContactsDataSource(initialContacts: List<Contact> = emptyList()) :
    LocalContactsDataSource {

    private val contacts = MutableStateFlow(initialContacts)

    override suspend fun insertContacts(contacts: List<Contact>) {
        this.contacts.value = (this.contacts.value + contacts).distinctBy { it.id }
    }

    override fun getAllContacts(): Flow<List<Contact>> = contacts
}
