package com.sebastianvm.contacts.dto

import com.sebastianvm.core.types.None
import com.sebastianvm.core.types.Option
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

/**
 * An email address.
 *
 * @property id Unique identifier.
 * @property address Email address value.
 * @property label Label to identify email address.
 */
@Serializable
data class EmailAddressRequest(
    val id: Option<Uuid> = None,
    val address: Option<String> = None,
    val label: Option<String?> = None,
)
