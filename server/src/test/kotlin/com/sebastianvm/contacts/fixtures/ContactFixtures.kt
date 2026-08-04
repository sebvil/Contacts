package com.sebastianvm.contacts.fixtures

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsRequest
import kotlin.uuid.Uuid

fun makeContact(): Contact = Contact(id = Uuid.random(), name = "Elliot")

fun makeContacts(): List<Contact> =
    listOf("Elliot", "Darlene", "Tyrell", "Angela").map {
        Contact(id = Uuid.random(), name = it)
    }

fun Contact.toContactsRequest(): ContactsRequest = ContactsRequest(id = id, name = name)
