package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.VCardComponent
import com.sebastianvm.contacts.vcard.escapeVCardText
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter

sealed interface VCardProperty<T> : VCardComponent {

    val name: String
    val value: T

    val parameters: List<VCardPropertyParameter<*>>
}

sealed interface TextVCardProperty : VCardProperty<String> {
    override fun toVCardString(): String {
        val escapedValue = escapeVCardText(value)
        return if (parameters.isEmpty()) {
            "$name:$escapedValue"
        } else {
            "$name;${parameters.joinToString(";") { it.toVCardString() }}:$escapedValue"
        }
    }
}

sealed interface StringVCardProperty : VCardProperty<String> {
    override fun toVCardString(): String =
        if (parameters.isEmpty()) {
            "$name:$value"
        } else {
            "$name;${parameters.joinToString(";") { it.toVCardString() }}:$value"
        }
}

sealed interface StructuredVCardProperty : VCardProperty<List<String>> {
    override fun toVCardString(): String {
        val encodedValue = value.joinToString(";") { escapeVCardText(it) }
        return if (parameters.isEmpty()) {
            "$name:$encodedValue"
        } else {
            "$name;${parameters.joinToString(";") { it.toVCardString() }}:$encodedValue"
        }
    }
}

sealed interface ListVCardProperty : VCardProperty<List<String>> {
    override fun toVCardString(): String {
        val encodedValue = value.joinToString(",") { escapeVCardText(it) }
        return if (parameters.isEmpty()) {
            "$name:$encodedValue"
        } else {
            "$name;${parameters.joinToString(";") { it.toVCardString() }}:$encodedValue"
        }
    }
}
