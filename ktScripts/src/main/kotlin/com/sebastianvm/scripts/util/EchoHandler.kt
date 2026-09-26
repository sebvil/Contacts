package com.sebastianvm.scripts.util

import com.github.ajalt.clikt.core.CliktCommand

interface EchoHandler {
    fun echo(message: Any?) {}
}

abstract class BaseCliktCommand(name: String? = null) : CliktCommand(name), EchoHandler {

    final override fun echo(message: Any?) {
        echo(message = message, trailingNewline = true)
    }
}
