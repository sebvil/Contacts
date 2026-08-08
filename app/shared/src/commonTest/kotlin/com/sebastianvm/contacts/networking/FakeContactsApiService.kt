package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.dto.ContactsResponse

class FakeContactsApiService(initialContacts: List<ContactsResponse> = emptyList()) :
    ContactsApiService {
    private val contacts = initialContacts.toMutableList()

    override suspend fun fetchContacts(): List<ContactsResponse> = contacts.toList()
}
