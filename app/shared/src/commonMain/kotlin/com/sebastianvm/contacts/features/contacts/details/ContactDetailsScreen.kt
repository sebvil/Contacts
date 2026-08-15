package com.sebastianvm.contacts.features.contacts.details

import com.sebastianvm.contacts.features.base.Parcelize
import com.sebastianvm.contacts.features.base.Screen
import kotlin.uuid.Uuid

@Parcelize data class ContactDetailsScreen(val contactId: Uuid) : Screen
