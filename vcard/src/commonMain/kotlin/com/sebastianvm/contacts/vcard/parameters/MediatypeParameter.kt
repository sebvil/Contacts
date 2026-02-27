package com.sebastianvm.contacts.vcard.parameters

data class MediatypeParameter(override val value: String) : VCardPropertyParameter<String> {
    override val name: String = "MEDIATYPE"
}

interface HasMediatype {
    val mediatype: MediatypeParameter?
}
