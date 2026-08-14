package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class FakeContactsRepository(
    initialContacts: List<Contact> = emptyList(),
    private val responseDelay: Duration = 1.milliseconds,
) : ContactsRepository {
    private val contacts = MutableStateFlow(initialContacts)

    var getContactsError: Throwable? = null

    override fun getContacts(): Flow<List<Contact>> = flow {
        // Suspend, like a real data source would, so collectors reliably observe an emitted Loading
        // state before
        // this flow resolves synchronously.
        delay(responseDelay)
        getContactsError?.let { throw it }
        emitAll(contacts)
    }

    override suspend fun refreshContacts() = Unit

    override suspend fun createContact(contact: Contact) {
        contacts.update { contactList ->
            (contactList + contact).distinctBy { it.id }
        }
    }
}
