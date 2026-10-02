package com.sebastianvm.contacts.dto

import com.sebastianvm.core.types.None
import com.sebastianvm.core.types.Option
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Represents information for a contact.
 *
 * @property id Unique identifier.
 * @property name Name of the contact.
 * @property birthday Contact's birthday.
 * @property emailAddresses Email addresses associated with this contact
 */
@Serializable
data class ContactRequest(
    val id: Option<Uuid> = None,
    val name: Option<String> = None,
    val birthday: Option<LocalDate?> = None,
    val emailAddresses: Option<List<EmailAddressRequest>> = None,
)
