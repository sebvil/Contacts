package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the type of entity that the vCard represents.
 *
 * This property is based on the semantics of the RFC 6350 6.1.4 (vCard 4.0).
 *
 * Valid values include: `individual`, `group`, `org`, `location`, `application`,
 * and x-name values.
 *
 * Example: `KIND:individual`
 *
 * @property value The kind value.
 */
data class KindProperty(override val value: String, val valueParam: ValueParameter? = null) :
    StringVCardProperty {
    override val name: String = "KIND"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam)
}
