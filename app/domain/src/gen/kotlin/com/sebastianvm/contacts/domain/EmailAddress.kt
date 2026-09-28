package com.sebastianvm.contacts.domain

import kotlin.uuid.Uuid

/**
 * An email address.
 *
 * @property id Unique identifier.
 * @property contactId ID of the contact to whom this address belongs.
 * @property address Email address value.
 * @property label Label to identify email address.
 */
data class EmailAddress(
    val id: Uuid,
    val contactId: Uuid,
    val address: String,
    val label: String? = null,
)
