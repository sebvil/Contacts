package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.AltIdParameter
import com.sebastianvm.contacts.vcard.parameters.GeoParameter
import com.sebastianvm.contacts.vcard.parameters.HasLanguage
import com.sebastianvm.contacts.vcard.parameters.HasType
import com.sebastianvm.contacts.vcard.parameters.LabelParameter
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.PidParameter
import com.sebastianvm.contacts.vcard.parameters.PrefParameter
import com.sebastianvm.contacts.vcard.parameters.TypeParameter
import com.sebastianvm.contacts.vcard.parameters.TzParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the delivery addresses for the object.
 *
 * This property is based on the semantics of the RFC 6350 6.3.1 (vCard 4.0), RFC 2426 3.2.1 (vCard
 * 3.0), and vCard 2.1 specifications.
 *
 * The structured property value corresponds to the following components:
 * - Post Office Box
 * - Extended Address (e.g., apartment or suite number)
 * - Street Address
 * - Locality (e.g., city)
 * - Region (e.g., state or province)
 * - Postal Code
 * - Country Name
 *
 * Example: `ADR;TYPE=work:;;100 Waters Edge;Baytown;LA;30314;United States of America`
 */
data class AddressProperty(
    override val value: List<String>,
    val valueParam: ValueParameter? = null,
    override val type: TypeParameter? = null,
    val pref: PrefParameter? = null,
    override val language: LanguageParameter? = null,
    val label: LabelParameter? = null,
    val geo: GeoParameter? = null,
    val tz: TzParameter? = null,
    val altId: AltIdParameter? = null,
    val pid: PidParameter? = null,
) : StructuredVCardProperty, HasType, HasLanguage {
    override val name: String = "ADR"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam, type, pref, language, label, geo, tz, altId, pid)

    val poBox: String
        get() = value.getOrElse(0) { "" }

    val extendedAddress: String
        get() = value.getOrElse(1) { "" }

    val street: String
        get() = value.getOrElse(2) { "" }

    val locality: String
        get() = value.getOrElse(LOCALITY_INDEX) { "" }

    val region: String
        get() = value.getOrElse(REGION_INDEX) { "" }

    val postalCode: String
        get() = value.getOrElse(POSTAL_CODE_INDEX) { "" }

    val country: String
        get() = value.getOrElse(COUNTRY_INDEX) { "" }

    companion object {
        private const val LOCALITY_INDEX = 3
        private const val REGION_INDEX = 4
        private const val POSTAL_CODE_INDEX = 5
        private const val COUNTRY_INDEX = 6
    }
}
