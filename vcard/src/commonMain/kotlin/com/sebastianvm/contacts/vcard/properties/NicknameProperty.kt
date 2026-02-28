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
 * Represents the text-based nicknames of the object.
 *
 * This property is based on the semantics of the RFC 6350 6.2.3 (vCard 4.0), RFC 2426 3.1.3 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `NICKNAME:Robbie`
 *
 * @property value The list of nicknames.
 */
data class NicknameProperty(
    override val value: List<String>,
    val valueParam: ValueParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    override val language: LanguageParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : ListVCardProperty, HasType, HasLanguage {
    override val name: String = "NICKNAME"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, type, pref, language, altId, pid)
}
