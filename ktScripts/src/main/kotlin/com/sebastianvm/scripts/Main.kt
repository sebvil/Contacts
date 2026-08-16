package com.sebastianvm.scripts

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.sebastianvm.scripts.codegen.codegenCommands
import com.sebastianvm.scripts.formatters.formatCommands

class Main : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = Main().subcommands(formatCommands(), codegenCommands()).main(args)
