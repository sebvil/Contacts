package com.sebastianvm.scripts.codegen

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.sebastianvm.scripts.codegen.module.MakeModule

class Codegen : CliktCommand() {
    override fun run() = Unit
}

fun codegenCommands(): CliktCommand = Codegen().subcommands(MakeModule())
