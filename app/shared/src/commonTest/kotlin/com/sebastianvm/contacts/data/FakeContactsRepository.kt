package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ContributesBinding(AppScope::class)
class FakeContactsRepository(initialContacts: List<Contact> = emptyList()) : ContactsRepository {
    private val contacts = initialContacts.toMutableList()

    var getContactsError: Throwable? = null

    override fun getContacts(): Flow<List<Contact>> = flow {
        getContactsError?.let { throw it }
        emit(contacts.toList())
    }
}
