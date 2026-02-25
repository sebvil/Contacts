package com.sebastianvm.contacts.vcard.parameters

data class PidParameter(override val value: String) : VCardPropertyParameter<String> {
    override val name: String = "PID"

    override fun validate(): String? {
        val match = Regex("\\d+(?:$|\\.\\d+)").matchEntire(value)
        return if (match == null) {
            "Invalid PID value"
        } else {
            null
        }
    }
}
