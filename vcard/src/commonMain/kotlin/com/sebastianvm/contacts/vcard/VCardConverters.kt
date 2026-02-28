package com.sebastianvm.contacts.vcard

fun String.toVCardString(): String =
    if ("," in this || ";" in this || ":" in this) {
        """"$this""""
    } else {
        this
    }

fun escapeVCardText(text: String): String =
    text
        .replace("\\", "\\\\")
        .replace(";", "\\;")
        .replace(",", "\\,")
        .replace("\n", "\\n")
        .replace("\r", "")

fun unescapeVCardText(text: String): String = buildString {
    var i = 0
    while (i < text.length) {
        if (text[i] == '\\' && i + 1 < text.length) {
            when (text[i + 1]) {
                '\\' -> append('\\')
                ';' -> append(';')
                ',' -> append(',')
                'n',
                'N' -> append('\n')
                else -> {
                    append('\\')
                    append(text[i + 1])
                }
            }
            i += 2
        } else {
            append(text[i])
            i++
        }
    }
}

fun splitStructuredValue(raw: String): List<String> =
    splitBy(raw, ';').map { unescapeVCardText(it) }

fun splitListValue(raw: String): List<String> = splitBy(raw, ',').map { unescapeVCardText(it) }

private fun splitBy(raw: String, delimiter: Char): List<String> {
    val components = mutableListOf<String>()
    val current = StringBuilder()
    var i = 0
    while (i < raw.length) {
        if (raw[i] == '\\' && i + 1 < raw.length) {
            current.append(raw[i])
            current.append(raw[i + 1])
            i += 2
        } else if (raw[i] == delimiter) {
            components.add(current.toString())
            current.clear()
            i++
        } else {
            current.append(raw[i])
            i++
        }
    }
    components.add(current.toString())
    return components
}
