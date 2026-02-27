package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents any XML data that is associated with the vCard.
 *
 * This property is based on the semantics of the RFC 6350 6.10.1 (vCard 4.0).
 *
 * Example: `XML:<data xmlns="http://www.example.com">...</data>`
 *
 * @property value The XML data.
 */
data class XmlProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty {
    override val name: String = "XML"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, altId)
}
