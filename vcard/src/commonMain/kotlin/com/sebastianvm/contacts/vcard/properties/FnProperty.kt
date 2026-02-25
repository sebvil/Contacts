package com.sebastianvm.contacts.vcard.properties

import com.sebastianvm.contacts.vcard.parameters.HasLanguage
import com.sebastianvm.contacts.vcard.parameters.LanguageParameter
import com.sebastianvm.contacts.vcard.parameters.VCardPropertyParameter

data class FnProperty(override val value: String, override val language: LanguageParameter?) :
    StringVCardProperty, HasLanguage {
    override val name: String = "FN"

    override val parameters: List<VCardPropertyParameter<*>>
        get() = listOfNotNull(language)
}
