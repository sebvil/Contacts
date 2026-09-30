package com.sebastianvm.contacts.dto

import com.sebastianvm.core.types.None
import com.sebastianvm.core.types.Option
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class ContactRequest(val id: Option<Uuid> = None, val name: Option<String> = None)
