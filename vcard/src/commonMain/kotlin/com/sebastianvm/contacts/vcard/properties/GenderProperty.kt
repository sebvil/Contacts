package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the sex and gender identity of the object.
 *
 * This property is based on the semantics of the RFC 6350 6.2.7 (vCard 4.0).
 *
 * The structured property value corresponds to:
 * - Sex (M, F, O, N, U)
 * - Gender identity (free text)
 *
 * Example: `GENDER:M;male`
 */
data class GenderProperty(
    override val value: List<String>,
    val valueParam: ValueParameter? = null,
) : StructuredVCardProperty {
    override val name: String = "GENDER"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam)

    val sex: String
        get() = value.getOrElse(0) { "" }

    val text: String
        get() = value.getOrElse(1) { "" }
}
