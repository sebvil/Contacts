package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasLanguage
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the organizational title or position of the object.
 *
 * This property is based on the semantics of the RFC 6350 6.6.1 (vCard 4.0), RFC 2426 3.5.1 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `TITLE:Director\, Research and Development`
 *
 * @property value The title text.
 */
data class TitleProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    override val language: LanguageParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : TextVCardProperty, HasType, HasLanguage {
    override val name: String = "TITLE"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, type, pref, language, altId, pid)
}
