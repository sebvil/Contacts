package com.sebastianvm.contacts.vcard

import com.sebastianvm.contacts.vcard.properties.FnProperty
import com.sebastianvm.contacts.vcard.properties.VCardProperty

sealed class VCard : VCardComponent {
    abstract val version: String
    abstract val properties: List<VCardProperty<*>>

    override fun toVCardString(): String = """
        BEGIN:VCARD
        VERSION:$version
        ${properties.joinToString("\n") { it.toVCardString() }}
        END:VCARD
    """.trimIndent()
}

data class V4VCard(
    val fn: FnProperty
) : VCard() {
    override val version: String = "4.0"
    override val properties: List<VCardProperty<*>> = listOfNotNull(fn)
}

data class V3VCard(
    val fn: FnProperty
) : VCard() {
    override val version: String = "3.0"
    override val properties: List<VCardProperty<*>> = listOfNotNull(fn)
}

data class V2VCard(
    val fn: FnProperty?
) : VCard() {
    override val version: String = "2.1"
    override val properties: List<VCardProperty<*>> = listOfNotNull(fn)
}