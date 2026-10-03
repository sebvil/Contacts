package com.sebastianvm.contacts.fixtures

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.core.types.Some
import kotlin.uuid.Uuid

fun makeContact(name: String = "Elliot"): Contact = Contact(id = Uuid.random(), name = name)

fun makeContacts(): List<Contact> =
    listOf("Elliot", "Darlene", "Tyrell", "Angela").map { makeContact(name = it) }

fun Contact.toContactsResponse(): ContactResponse = ContactResponse(id = id, name = Some(name))
