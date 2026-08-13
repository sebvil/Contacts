package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.app.database.LocalContactsDataSource
import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.networking.ContactsApiService
import com.sebastianvm.contacts.networking.toContact
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.Flow

@ContributesBinding(AppScope::class)
internal class OfflineFirstContactRepository(
    localContactsDataSource: LocalContactsDataSource,
    contactsApiService: ContactsApiService,
) : ContactsRepository {

    private val contactsSource =
        OfflineFirstSource(
            readLocal = localContactsDataSource::getAllContacts,
            fetchRemote = contactsApiService::fetchContacts,
            persist = { contacts: List<ContactsResponse> ->
                localContactsDataSource.insertContacts(contacts.map(ContactsResponse::toContact))
            },
        )

    override fun getContacts(): Flow<List<Contact>> = contactsSource.observe()

    override suspend fun refreshContacts() {
        contactsSource.refresh()
    }
}
