package com.sebastianvm.contacts.dto

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.core.types.Some

fun Contact.toContactsResponse(): ContactsResponse = ContactsResponse(id = id, name = Some(name))
