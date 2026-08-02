package com.sebastianvm.contacts.routes.dtos

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
public data class ContactsResponse(val id: Uuid, val name: String)