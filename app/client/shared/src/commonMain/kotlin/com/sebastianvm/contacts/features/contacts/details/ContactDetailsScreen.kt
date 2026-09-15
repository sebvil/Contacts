package com.sebastianvm.contacts.features.contacts.details

import com.sebastianvm.contacts.features.base.Screen
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable data class ContactDetailsScreen(val contactId: Uuid) : Screen
