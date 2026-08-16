package com.sebastianvm.contacts.dto

import com.sebastianvm.contacts.domain.Contact

fun Contact.toContactsResponse(): ContactsResponse = ContactsResponse(id = id, name = name)
