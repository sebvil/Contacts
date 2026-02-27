package com.sebastianvm.contacts.vcard.parameters

data class PrefParameter(override val value: Int) : VCardPropertyParameter<Int> {
    override val name: String = "PREF"

    override fun validate(): String? {
        if (value !in 1..100) {
            return "Preference value must be between 1 and 100"
        }
        return null
    }
}
