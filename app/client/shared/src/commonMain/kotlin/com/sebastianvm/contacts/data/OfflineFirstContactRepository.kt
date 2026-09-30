package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.app.database.LocalContactsDataSource
import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.contacts.networking.ContactsApiService
import com.sebastianvm.contacts.networking.toContact
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

@ContributesBinding(AppScope::class)
internal class OfflineFirstContactRepository(
    private val localContactsDataSource: LocalContactsDataSource,
    private val contactsApiService: ContactsApiService,
) : ContactsRepository {

    private val contactsSource =
        OfflineFirstSource(
            readLocal = localContactsDataSource::getAllContacts,
            fetchRemote = contactsApiService::fetchContacts,
            persist = { contacts: List<ContactResponse> ->
                localContactsDataSource.insertContacts(contacts.map(ContactResponse::toContact))
            },
        )

    override fun getContacts(): Flow<List<Contact>> = contactsSource.observe()

    override suspend fun refreshContacts() {
        contactsSource.refresh()
    }

    override suspend fun createContact(contactName: String) {
        val _ = contactsApiService.createContact(Contact(id = Uuid.random(), name = contactName))
    }

    override fun getContact(id: Uuid): Flow<Contact> = localContactsDataSource.getContact(id)
}
