package com.sebastianvm.scripts.formatters

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.sebastianvm.scripts.formatters.versions.FormatVersionCatalogs

class Format : CliktCommand() {
    override fun run() = Unit
}

fun formatCommands(): CliktCommand = Format().subcommands(FormatVersionCatalogs())
