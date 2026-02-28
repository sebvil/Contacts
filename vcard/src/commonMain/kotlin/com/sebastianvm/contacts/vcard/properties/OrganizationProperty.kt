package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.HasLanguage
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.SortAsParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the name and units of the organization associated with the object.
 *
 * This property is based on the semantics of the RFC 6350 6.6.4 (vCard 4.0), RFC 2426 3.5.5 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * The structured property value corresponds to the organization name followed by zero or more
 * organizational units.
 *
 * Example: `ORG:ABC\, Inc.;North American Division;Marketing`
 *
 * @property value The list of organization components.
 */
data class OrganizationProperty(
    override val value: List<String>,
    val valueParam: ValueParameter? = null,
    val sortAs: SortAsParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    override val language: LanguageParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : StructuredVCardProperty, HasType, HasLanguage {
    override val name: String = "ORG"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, sortAs, type, pref, language, altId, pid)

    /** The name of the organization */
    val organizationName: String
        get() = value.getOrElse(0) { "" }

    /** Any organizational units associated with the vCard entity */
    val organizationalUnits: List<String>
        get() = if (value.size > 1) value.drop(1) else emptyList()
}
