package com.sebastianvm.contacts.domain

import kotlin.uuid.Uuid

data class Contact(val id: Uuid = Uuid.random(), val name: String)
