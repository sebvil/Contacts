package com.sebastianvm.contacts.routes.dtos

import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable public data class ContactsRequest(val id: Uuid, val name: String)
