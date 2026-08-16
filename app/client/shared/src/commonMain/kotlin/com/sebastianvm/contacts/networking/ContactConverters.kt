package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsResponse

internal fun ContactsResponse.toContact(): Contact = Contact(id = id, name = name)
