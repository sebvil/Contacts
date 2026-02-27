package com.sebastianvm.contacts.vcard.parameters

data class GeoParameter(override val value: String) : VCardPropertyParameter<String> {
    override val name: String = "GEO"

    override fun toVCardString(): String = "$name=\"$value\""
}
