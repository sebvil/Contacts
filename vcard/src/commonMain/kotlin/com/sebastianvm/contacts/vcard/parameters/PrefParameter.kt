package com.sebastianvm.contacts.vcard.parameters

data class PrefParameter(override val value: Int) : VCardPropertyParameter<Int> {
    override val name: String = "PREF"

    override fun validate(): String? {
        if (value !in 1..MAX_VALUE) {
            return "Preference value must be between 1 and $MAX_VALUE"
        }
        return null
    }

    companion object {
        private const val MAX_VALUE = 100
    }
}
