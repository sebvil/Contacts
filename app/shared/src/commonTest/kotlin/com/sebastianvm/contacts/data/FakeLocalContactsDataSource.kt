package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.app.database.LocalContactsDataSource
import com.sebastianvm.contacts.domain.Contact
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeLocalContactsDataSource(initialContacts: List<Contact> = emptyList()) :
    LocalContactsDataSource {

    private val contacts = MutableStateFlow(initialContacts)

    override suspend fun insertContacts(contacts: List<Contact>) {
        this.contacts.value = (this.contacts.value + contacts).distinctBy { it.id }
    }

    override fun getAllContacts(): Flow<List<Contact>> = contacts

    override fun getContact(id: Uuid): Flow<Contact> {
        return contacts.map {
            it.find { contact -> contact.id == id } ?: throw NoSuchElementException()
        }
    }
}
