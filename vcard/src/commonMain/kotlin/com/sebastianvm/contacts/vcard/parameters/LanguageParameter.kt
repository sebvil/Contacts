package com.sebastianvm.contacts.vcard.parameters

data class LanguageParameter(override val value: String) : StringVCardParameter {
    override val name: String = "LANGUAGE"
}

interface HasLanguage {
    val language: LanguageParameter?
}
