package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the application categories that the object belongs to.
 *
 * This property is based on the semantics of the RFC 6350 6.7.1 (vCard 4.0), RFC 2426 3.6.1 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * Example: `CATEGORIES:TRAVEL AGENT,INTERNET,HOTEL`
 */
data class CategoriesProperty(
    override val value: List<String>,
    val valueParam: ValueParameter? = null,
    val pid: PidParameter? = null,
    val pref: PrefParameter? = null,
    override val type: TypeParameter? = null,
    val altId: AltIdParameter? = null,
) : ListVCardProperty, HasType {
    override val name: String = "CATEGORIES"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, pid, pref, type, altId)
}
