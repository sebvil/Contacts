package com.sebastianvm.contacts.vcard.parameters

import com.sebastianvm.contacts.vcard.VCardComponent
import com.sebastianvm.contacts.vcard.toVCardString

sealed interface VCardPropertyParameter<T> : VCardComponent {
    val name: String

    val value: T

    fun valueToVCardString(): String = value.toString()

    override fun toVCardString(): String = "$name=${valueToVCardString()}"
}

sealed interface StringVCardParameter : VCardPropertyParameter<String> {
    override fun valueToVCardString(): String = value.toVCardString()
}
