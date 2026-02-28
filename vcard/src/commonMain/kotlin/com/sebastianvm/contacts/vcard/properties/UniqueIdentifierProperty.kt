package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents a value that represents a globally unique identifier corresponding to the entity.
 *
 * This property is based on the semantics of the RFC 6350 6.7.6 (vCard 4.0), RFC 2426 3.6.7 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `UID:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6`
 *
 * @property value The unique identifier.
 */
data class UniqueIdentifierProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
) : StringVCardProperty {
    override val name: String = "UID"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam)
}
