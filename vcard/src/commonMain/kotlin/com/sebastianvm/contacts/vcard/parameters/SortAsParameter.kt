package com.sebastianvm.contacts.vcard.parameters

data class SortAsParameter(override val value: List<String>) :
    VCardPropertyParameter<List<String>> {
    override val name: String = "SORT-AS"

    override fun valueToVCardString(): String = value.joinToString(",")

    override fun toVCardString(): String = "$name=\"${valueToVCardString()}\""
}
