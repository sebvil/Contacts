package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.fixtures.toContactsResponse

class FakeContactsApiService(initialContacts: List<ContactsResponse> = emptyList()) :
    ContactsApiService {
    private val contacts = initialContacts.toMutableList()

    var fetchContactsError: Throwable? = null

    override suspend fun fetchContacts(): Result<List<ContactsResponse>> =
        fetchContactsError?.let { Result.failure(it) } ?: Result.success(contacts.toList())

    override suspend fun createContact(contact: Contact): Result<ContactsResponse> {
        val contactResponse = contact.toContactsResponse()
        contacts.add(contactResponse)
        return Result.success(contactResponse)
    }
}
