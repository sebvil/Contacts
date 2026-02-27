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
 * Represents the URLs for the entity's free/busy time.
 *
 * This property is based on the semantics of the RFC 6350 6.9.1 (vCard 4.0).
 *
 * Example: `FBURL;MEDIATYPE=text/calendar:https://www.example.com/calendar/busy`
 *
 * @property value The free/busy URL.
 */
data class FreeBusyUrlProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val type: TypeParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty, HasType, HasMediatype {
    override val name: String = "FBURL"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, mediatype, pid, pref, type, altId)
}
