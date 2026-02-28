package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.CalscaleParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the date of marriage, or equivalent, of the object.
 *
 * This property is based on the semantics of the RFC 6350 6.2.6 (vCard 4.0).
 *
 * Example: `ANNIVERSARY:19960415`
 */
data class AnniversaryProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    val altId: AltIdParameter? = null,
    val calscale: CalscaleParameter? = null,
) : StringVCardProperty {
    override val name: String = "ANNIVERSARY"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, altId, calscale)
}
