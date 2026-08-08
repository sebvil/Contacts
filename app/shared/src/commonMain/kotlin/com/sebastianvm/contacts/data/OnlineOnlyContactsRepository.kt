package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.networking.ContactsApiService
import com.sebastianvm.contacts.networking.toContact
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding

@ContributesBinding(AppScope::class)
internal class OnlineOnlyContactsRepository(private val contactsApiService: ContactsApiService) :
    ContactsRepository {
    override suspend fun getContacts(): List<Contact> =
        contactsApiService.fetchContacts().map { it.toContact() }
}
