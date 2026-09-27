package com.sebastianvm.contacts.domain

import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate

/**
 * Represents information for a contact.
 *
 * @property id Unique identifier.
 * @property name Name of the contact.
 * @property birthday Contact's birthday.
 */
data class Contact(
    val id: Uuid,
    val name: String,
    val birthday: LocalDate? = null,
)
