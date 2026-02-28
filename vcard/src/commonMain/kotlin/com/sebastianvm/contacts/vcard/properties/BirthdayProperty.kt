package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.CalscaleParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the date of birth of the individual associated with the vCard.
 *
 * This property is based on the semantics of the RFC 6350 6.2.5 (vCard 4.0), RFC 2426 3.1.5 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `BDAY:19960415`
 *
 * @property value The date of birth.
 */
data class BirthdayProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    val altId: AltIdParameter? = null,
    val calscale: CalscaleParameter? = null,
) : StringVCardProperty {
    override val name: String = "BDAY"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, altId, calscale)
}
