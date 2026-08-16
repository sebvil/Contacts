package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsResponse

internal interface ContactsApiService {

    suspend fun fetchContacts(): Result<List<ContactsResponse>>

    suspend fun createContact(contact: Contact): Result<ContactsResponse>
}
