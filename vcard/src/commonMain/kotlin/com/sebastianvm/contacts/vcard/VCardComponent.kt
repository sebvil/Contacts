package com.sebastianvm.contacts.vcard

interface VCardComponent {
    fun toVCardString(): String

    /** Returns null if the component is valid, otherwise returns the error message */
    fun validate(): String? = null
}
