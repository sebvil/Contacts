package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the electronic mail addresses for communication with the object.
 *
 * This property is based on the semantics of the RFC 6350 6.4.2 (vCard 4.0), RFC 2426 3.3.2 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `EMAIL;TYPE=work:jqpublic@xyz.example.com`
 *
 * @property value The email address.
 */
data class EmailProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : TextVCardProperty, HasType {
    override val name: String = "EMAIL"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, type, pref, altId, pid)
}
