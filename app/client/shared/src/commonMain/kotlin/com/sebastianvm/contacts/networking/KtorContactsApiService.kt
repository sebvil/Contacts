package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsRequest
import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.routes.Contacts
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.ktor.client.HttpClient

@ContributesBinding(AppScope::class)
internal class KtorContactsApiService(private val client: HttpClient) : ContactsApiService {
    override suspend fun fetchContacts(): Result<List<ContactsResponse>> = client.get(Contacts)

    override suspend fun createContact(contact: Contact): Result<ContactsResponse> =
        client.post(resource = Contacts, body = contact.toContactsRequest())
}

fun Contact.toContactsRequest(): ContactsRequest = ContactsRequest(id = id, name = name)
