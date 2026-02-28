package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the identifier for the product that created the vCard.
 *
 * This property is based on the semantics of the RFC 6350 6.7.3 (vCard 4.0), RFC 2426 3.6.3 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `PRODID:-//ONLINE DIRECTORY//NONSGML Version 1//EN`
 *
 * @property value The product identifier.
 */
data class ProductIdentifierProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
) : StringVCardProperty {
    override val name: String = "PRODID"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam)
}
