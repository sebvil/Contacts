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
 * Represents the telephone numbers for communication with the object.
 *
 * This property is based on the semantics of the RFC 6350 6.4.1 (vCard 4.0),
 * RFC 2426 3.3.1 (vCard 3.0), and vCard 2.1 specifications.
 *
 * Example: `TEL;VALUE=uri;TYPE=home:tel:+1-555-555-5555`
 *
 * @property value The telephone number or URI.
 */
data class TelephoneProperty(
    override val value: String,
    override val type: TypeParameter? = null,
    val valueParam: ValueParameter? = null,
    val pref: PrefParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : StringVCardProperty, HasType, HasMediatype {
    override val name: String = "TEL"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(type, valueParam, pref, mediatype, altId, pid)
}
