package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactRequest
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.contacts.routes.ContactsRoute
import com.sebastianvm.core.types.Some
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.ktor.client.HttpClient

@ContributesBinding(AppScope::class)
internal class KtorContactsApiService(private val client: HttpClient) : ContactsApiService {
    override suspend fun fetchContacts(): Result<List<ContactResponse>> = client.get(ContactsRoute)

    override suspend fun createContact(contact: Contact): Result<ContactResponse> =
        client.post(resource = ContactsRoute, body = contact.toContactsRequest())
}

fun Contact.toContactsRequest(): ContactRequest = ContactRequest(id = Some(id), name = Some(name))
