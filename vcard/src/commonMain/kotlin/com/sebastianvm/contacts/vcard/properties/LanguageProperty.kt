package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the languages that may be used for contacting the entity.
 *
 * This property is based on the semantics of the RFC 6350 6.4.4 (vCard 4.0).
 *
 * Example: `LANG;TYPE=work;PREF=1:en`
 *
 * @property value The language tag.
 */
data class LanguageProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val type: TypeParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty, HasType {
    override val name: String = "LANG"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, pid, pref, type, altId)
}
