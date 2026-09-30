package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactResponse

internal interface ContactsApiService {

    suspend fun fetchContacts(): Result<List<ContactResponse>>

    suspend fun createContact(contact: Contact): Result<ContactResponse>
}
