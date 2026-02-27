package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasMediatype
import com.sebastianvm.contacts.vcard.parameters.MediatypeParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents URIs that may be used to obtain the vCard.
 *
 * This property is based on the semantics of the RFC 6350 6.1.3 (vCard 4.0).
 *
 * Example: `SOURCE:ldap://ldap.example.com/cn=Babs%20Jensen,o=University%20of%20Michigan,c=US`
 *
 * @property value The source URI.
 */
data class SourceProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty, HasMediatype {
    override val name: String = "SOURCE"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, pid, pref, mediatype, altId)
}
