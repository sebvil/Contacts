package com.sebastianvm.contacts.dto

import com.sebastianvm.contacts.domain.Contact

fun ContactsRequest.toContact(): Contact = Contact(id = id, name = name)
