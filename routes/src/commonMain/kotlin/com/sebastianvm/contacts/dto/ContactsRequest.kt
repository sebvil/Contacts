package com.sebastianvm.contacts.dto

import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable data class ContactsRequest(val id: Uuid, val name: String)
