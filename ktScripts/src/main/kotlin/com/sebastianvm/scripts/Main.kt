package com.sebastianvm.scripts

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.sebastianvm.scripts.formatters.Format
import com.sebastianvm.scripts.formatters.versions.FormatVersionCatalogs


class Main : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = Main()
    .subcommands(Format().subcommands(FormatVersionCatalogs()))
    .main(args)