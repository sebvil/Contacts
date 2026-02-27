package com.sebastianvm.contacts.vcard.parser

object LineUnfolder {
    fun unfold(input: String): List<String> {
        val normalized = input.replace("\r\n", "\n").replace("\r", "\n")
        val result = mutableListOf<String>()
        val current = StringBuilder()

        val lines = normalized.split("\n")
        for (i in lines.indices) {
            val line = lines[i]
            if (line.isEmpty()) continue
            if (line.startsWith(" ") || line.startsWith("\t")) {
                current.append(line.substring(1))
            } else {
                if (current.isNotEmpty()) {
                    result.add(current.toString())
                }
                current.setLength(0)
                current.append(line)
            }
        }
        if (current.isNotEmpty()) {
            result.add(current.toString())
        }

        return result.filter { it.isNotBlank() }
    }
}
