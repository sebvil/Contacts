package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.core.types.getOrElse

internal fun ContactResponse.toContact(): Contact = Contact(id = id, name = name.getOrElse { "" })
