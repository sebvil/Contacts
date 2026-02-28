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
 * Represents supplemental information or a comment that is associated with the vCard.
 *
 * This property is based on the semantics of the RFC 6350 6.7.2 (vCard 4.0), RFC 2426 3.6.2 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `NOTE:This vCard is\n for a person with a\n really long note.`
 *
 * @property value The note text.
 */
data class NoteProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    override val language: LanguageParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : TextVCardProperty, HasLanguage, HasType {
    override val name: String = "NOTE"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, type, pref, language, altId, pid)
}
