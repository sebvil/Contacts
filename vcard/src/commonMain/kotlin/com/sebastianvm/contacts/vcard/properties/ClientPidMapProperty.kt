package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter

/**
 * Represents the mapping between a PID and its URI.
 *
 * This property is based on the semantics of the RFC 6350 6.7.7 (vCard 4.0).
 *
 * Example: `CLIENTPIDMAP:1;urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6`
 */
data class ClientPidMapProperty(override val value: List<String>) : StructuredVCardProperty {
    override val name: String = "CLIENTPIDMAP"

    override val parameters: List<VCardPropertyParameter<*>> = emptyList()

    /** The PID number */
    val pidNumber: String
        get() = value.getOrElse(0) { "" }

    /** The URI associated with the PID */
    val uri: String
        get() = value.getOrElse(1) { "" }
}
