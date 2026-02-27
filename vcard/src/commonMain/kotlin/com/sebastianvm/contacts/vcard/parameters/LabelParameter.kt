package com.sebastianvm.contacts.vcard.parameters

data class LabelParameter(override val value: String) : StringVCardParameter {
    override val name: String = "LABEL"
}
