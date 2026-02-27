package com.sebastianvm.contacts.vcard.parameters

data class ValueParameter(override val value: Value) : VCardPropertyParameter<Value> {
    override val name: String
        get() = "VALUE"

    override fun valueToVCardString(): String = value.vCardString
}

enum class Value(val vCardString: String) {
    Text("text"),
    Uri("uri"),
    Date("date"),
    Time("time"),
    DateTime("date-time"),
    DateAndOrTime("date-and-or-time"),
    Timestamp("timestamp"),
    Boolean("boolean"),
    Integer("integer"),
    Float("float"),
    UtcOffset("utc-offset"),
    LanguageTag("language-tag"),
}
