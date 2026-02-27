package com.sebastianvm.contacts.vcard.parameters

data class TypeParameter(override val value: List<String>) : VCardPropertyParameter<List<String>> {
    override val name: String = "TYPE"

    override fun valueToVCardString(): String = value.joinToString(",")

    override fun toVCardString(): String = "$name=${valueToVCardString()}"
}

interface HasType {
    val type: TypeParameter?
}
