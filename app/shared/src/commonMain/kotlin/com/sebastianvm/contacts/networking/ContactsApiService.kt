package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.dto.ContactsResponse

internal interface ContactsApiService {

    suspend fun fetchContacts(): List<ContactsResponse>
}
