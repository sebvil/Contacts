package com.sebastianvm.contacts.fixtures

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactRequest
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.core.types.Some
import com.sebastianvm.core.types.getOrThrow
import kotlin.uuid.Uuid

fun makeContact(name: String = "Elliot"): Contact = Contact(id = Uuid.random(), name = name)

fun makeContacts(): List<Contact> =
    listOf("Elliot", "Darlene", "Tyrell", "Angela").map { makeContact(name = it) }

fun Contact.toContactsRequest(): ContactRequest = ContactRequest(id = Some(id), name = Some(name))

fun Contact.toContactsResponse(): ContactResponse = ContactResponse(id = id, name = Some(name))

fun makeContactResponse(name: String = "Elliot"): ContactResponse =
    makeContact(name).toContactsResponse()

fun makeContactRequest(name: String = "Elliot"): ContactRequest =
    makeContact(name).toContactsRequest()

fun makeContactRequests(): List<ContactRequest> =
    listOf("Elliot", "Darlene", "Tyrell", "Angela").map { makeContactRequest(name = it) }

fun ContactRequest.toContactsResponse(): ContactResponse =
    ContactResponse(id = id.getOrThrow(), name = name)
