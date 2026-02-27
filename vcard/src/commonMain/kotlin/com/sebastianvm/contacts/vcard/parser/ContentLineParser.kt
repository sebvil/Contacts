package com.sebastianvm.contacts.vcard.parser

object ContentLineParser {

    fun parse(line: String): ContentLine {
        var i = 0
        val len = line.length

        // Parse group and name
        val nameEnd = findNameEnd(line)
        val nameSection = line.substring(0, nameEnd)
        val group: String?
        val name: String

        val dotIndex = nameSection.indexOf('.')
        if (dotIndex >= 0) {
            group = nameSection.substring(0, dotIndex)
            name = nameSection.substring(dotIndex + 1)
        } else {
            group = null
            name = nameSection
        }

        i = nameEnd

        // Parse parameters
        val parameters = mutableListOf<ParsedParameter>()
        while (i < len && line[i] == ';') {
            i++ // skip ';'
            val param = parseParameter(line, i)
            parameters.add(param.first)
            i = param.second
        }

        // Skip ':'
        val value =
            if (i < len && line[i] == ':') {
                line.substring(i + 1)
            } else {
                ""
            }

        return ContentLine(
            group = group,
            name = name.uppercase(),
            parameters = parameters,
            value = value,
        )
    }

    private fun findNameEnd(line: String): Int {
        for (i in line.indices) {
            if (line[i] == ';' || line[i] == ':') return i
        }
        return line.length
    }

    private fun parseParameter(line: String, start: Int): Pair<ParsedParameter, Int> {
        var i = start
        val len = line.length

        // Find parameter name
        val nameStart = i
        while (i < len && line[i] != '=' && line[i] != ';' && line[i] != ':') {
            i++
        }
        val paramName = line.substring(nameStart, i).uppercase()

        if (i >= len || line[i] != '=') {
            // Bare parameter (no value) - treat the name as the value with TYPE as the name
            return ParsedParameter("TYPE", paramName) to i
        }

        i++ // skip '='

        // Parse parameter value (may be quoted)
        val value =
            if (i < len && line[i] == '"') {
                i++ // skip opening quote
                val valueStart = i
                while (i < len && line[i] != '"') {
                    i++
                }
                val v = line.substring(valueStart, i)
                if (i < len) i++ // skip closing quote
                v
            } else {
                val valueStart = i
                while (i < len && line[i] != ';' && line[i] != ':') {
                    i++
                }
                line.substring(valueStart, i)
            }

        return ParsedParameter(paramName, value) to i
    }
}
