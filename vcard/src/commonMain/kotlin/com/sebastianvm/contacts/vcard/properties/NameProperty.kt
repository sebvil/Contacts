package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasLanguage
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.SortAsParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the components of the name of the object.
 *
 * This property is based on the semantics of the RFC 6350 6.2.2 (vCard 4.0),
 * RFC 2426 3.1.2 (vCard 3.0), and vCard 2.1 specifications.
 *
 * The structured property value corresponds to the following components:
 * - Family Name
 * - Given Names
 * - Additional Names
 * - Honorific Prefixes
 * - Honorific Suffixes
 *
 * Example: `N:Public;John;Quinlan;Mr.;Esq.`
 *
 * @property value The list of name components.
 */
data class NameProperty(
    override val value: List<String>,
    val valueParam: ValueParameter? = null,
    val sortAs: SortAsParameter? = null,
    override val language: LanguageParameter? = null,
    val altId: AltIdParameter? = null,
) : StructuredVCardProperty, HasLanguage {
    override val name: String = "N"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, sortAs, language, altId)

    val familyName: String
        get() = value.getOrElse(0) { "" }

    val givenName: String
        get() = value.getOrElse(1) { "" }

    val additionalNames: String
        get() = value.getOrElse(2) { "" }

    val honorificPrefixes: String
        get() = value.getOrElse(3) { "" }

    val honorificSuffixes: String
        get() = value.getOrElse(4) { "" }
}
