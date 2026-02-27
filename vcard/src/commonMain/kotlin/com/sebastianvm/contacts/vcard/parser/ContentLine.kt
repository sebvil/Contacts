package com.sebastianvm.contacts.vcard.parser

data class ParsedParameter(val name: String, val value: String)

data class ContentLine(
    val group: String?,
    val name: String,
    val parameters: List<ParsedParameter>,
    val value: String,
)
