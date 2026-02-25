package com.sebastianvm.contacts.vcard

fun String.toVCardString(): String = if ("," in this || ";" in this || ":" in this) {
    """"$this""""
} else {
    this
}