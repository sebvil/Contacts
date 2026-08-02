package com.sebastianvm.contacts.dto

import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable public data class ContactsRequest(val id: Uuid, val name: String)
