package com.sebastianvm.contacts.vcard.parameters

data class TzParameter(override val value: String) : VCardPropertyParameter<String> {
    override val name: String = "TZ"

    override fun toVCardString(): String = "$name=\"$value\""
}
