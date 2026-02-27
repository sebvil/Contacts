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
 * Represents other entities that the object is associated with.
 *
 * This property is based on the semantics of the RFC 6350 6.6.6 (vCard 4.0).
 *
 * Example: `RELATED;TYPE=friend:urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6`
 *
 * @property value The related URI.
 */
data class RelatedProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val type: TypeParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty, HasType, HasMediatype {
    override val name: String = "RELATED"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, mediatype, pid, pref, type, altId)
}
