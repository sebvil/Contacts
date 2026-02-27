package com.sebastianvm.contacts.vcard.parameters

data class CalscaleParameter(override val value: String) : VCardPropertyParameter<String> {
    override val name: String = "CALSCALE"
}
