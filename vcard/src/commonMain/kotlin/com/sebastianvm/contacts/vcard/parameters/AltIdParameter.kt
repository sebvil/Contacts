package com.sebastianvm.contacts.vcard.parameters

data class AltIdParameter(override val value: String) : VCardPropertyParameter<String> {
    override val name: String = "ALTID"
}
