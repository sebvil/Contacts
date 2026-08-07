package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.routes.Contacts
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

@ContributesBinding(AppScope::class)
internal class KtorContactsApiService(private val client: HttpClient) : ContactsApiService {
    override suspend fun fetchContacts(): List<ContactsResponse> =
        client.get(Contacts).body<List<ContactsResponse>>()
}
