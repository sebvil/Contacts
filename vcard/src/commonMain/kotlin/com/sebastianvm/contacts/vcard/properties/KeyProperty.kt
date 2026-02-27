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
 * Represents the public keys or certificates associated with the object.
 *
 * This property is based on the semantics of the RFC 6350 6.8.1 (vCard 4.0),
 * RFC 2426 3.7.1 (vCard 3.0), and vCard 2.1 specifications.
 *
 * Example: `KEY;VALUE=uri:https://www.example.com/keys/jdoe.cer`
 *
 * @property value The key URI or base64 encoded data.
 */
data class KeyProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val type: TypeParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty, HasType, HasMediatype {
    override val name: String = "KEY"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, pid, pref, type, mediatype, altId)
}
