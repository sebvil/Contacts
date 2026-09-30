package com.sebastianvm.contacts.dto

import com.sebastianvm.core.types.Option
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable data class ContactsResponse(val id: Uuid, val name: Option<String>)
