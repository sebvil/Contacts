package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasLanguage
import com.sebastianvm.contacts.vcard.parameters.HasMediatype
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.MediatypeParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents digital sound content that annotates some aspect of the object.
 *
 * This property is based on the semantics of the RFC 6350 6.7.5 (vCard 4.0), RFC 2426 3.6.5 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `SOUND;MEDIATYPE=audio/basic:https://www.example.com/sounds/jdoe.au`
 */
data class SoundProperty(
    override val value: String,
    val valueParam: ValueParameter? = null,
    override val language: LanguageParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val type: TypeParameter? = null,
    override val mediatype: MediatypeParameter? = null,
    val altId: AltIdParameter? = null,
) : StringVCardProperty, HasType, HasLanguage, HasMediatype {
    override val name: String = "SOUND"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, language, pid, pref, type, mediatype, altId)
}
