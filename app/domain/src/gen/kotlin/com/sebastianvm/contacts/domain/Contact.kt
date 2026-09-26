package com.sebastianvm.contacts.domain

import kotlin.uuid.Uuid

/**
 * Represents information for a contact.
 *
 * @param id Unique identifier.
 * @param name Name of the contact.
 */
data class Contact(
    val id: Uuid,
    val name: String,
)
