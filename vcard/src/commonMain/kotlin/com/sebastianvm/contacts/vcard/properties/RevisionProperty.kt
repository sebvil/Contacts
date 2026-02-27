package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter
import com.sebastianvm.contacts.vcard.parameters.ValueParameter

/**
 * Represents the revision date and time when the vCard was last updated.
 *
 * This property is based on the semantics of the RFC 6350 6.7.4 (vCard 4.0),
 * RFC 2426 3.6.4 (vCard 3.0), and vCard 2.1 specifications.
 *
 * Example: `REV:19951031T222710Z`
 *
 * @property value The revision timestamp.
 */
data class RevisionProperty(override val value: String, val valueParam: ValueParameter? = null) :
    StringVCardProperty {
    override val name: String = "REV"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(valueParam)
}
