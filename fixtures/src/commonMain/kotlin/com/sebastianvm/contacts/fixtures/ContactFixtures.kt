package com.sebastianvm.contacts.fixtures

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsRequest
import com.sebastianvm.contacts.dto.ContactsResponse
import kotlin.uuid.Uuid

public fun makeContact(name: String = "Elliot"): Contact = Contact(id = Uuid.random(), name = name)

public fun makeContacts(): List<Contact> =
    listOf("Elliot", "Darlene", "Tyrell", "Angela").map { makeContact(name = it) }

public fun Contact.toContactsRequest(): ContactsRequest = ContactsRequest(id = id, name = name)

public fun Contact.toContactsResponse(): ContactsResponse = ContactsResponse(id = id, name = name)
