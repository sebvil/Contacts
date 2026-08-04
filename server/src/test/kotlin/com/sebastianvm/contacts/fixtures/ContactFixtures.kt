package com.sebastianvm.contacts.fixtures

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsRequest
import kotlin.uuid.Uuid

fun makeContact(): Contact = Contact(Uuid.random(), "Elliot")

fun Contact.toContactsRequest(): ContactsRequest = ContactsRequest(id = id, name = name)
