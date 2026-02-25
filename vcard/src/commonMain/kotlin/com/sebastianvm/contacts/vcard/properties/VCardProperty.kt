package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.VCardComponent
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter

sealed interface VCardProperty<T> : VCardComponent {

    val name: String
    val value: T

    val parameters: List<VCardPropertyParameter<*>>
}

sealed interface StringVCardProperty : VCardProperty<String> {
    override fun toVCardString(): String = if (parameters.isEmpty()) {
        "$name:$value"
    } else {
        "$name;${parameters.joinToString(";") { it.toVCardString() }}:$value"
    }
}