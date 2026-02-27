package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasMediatype
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.MediatypeParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents URLs that may be used to obtain real-time information about the object.
 *
 * This property is based on the semantics of the RFC 6350 6.7.8 (vCard 4.0),
 * RFC 2426 3.6.8 (vCard 3.0), and vCard 2.1 specifications.
 *
 * Example: `URL:https://www.example.com`
 *
 * @property value The URL.
 */
data class UrlProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : StringVCardProperty, HasType, HasMediatype {
    override val name: String = "URL"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, type, pref, mediatype, altId, pid)
}
