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
 * Represents an image or photograph of the object.
 *
 * This property is based on the semantics of the RFC 6350 6.2.4 (vCard 4.0),
 * RFC 2426 3.1.4 (vCard 3.0), and vCard 2.1 specifications.
 *
 * Example: `PHOTO;MEDIATYPE=image/jpeg:https://www.example.com/pub/photos/jqpublic.jpg`
 *
 * @property value The photo URI or base64 encoded data.
 */
data class PhotoProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : StringVCardProperty, HasType, HasMediatype {
    override val name: String = "PHOTO"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, type, pref, mediatype, altId, pid)
}
