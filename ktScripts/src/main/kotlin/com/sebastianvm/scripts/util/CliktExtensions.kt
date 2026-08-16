package com.sebastianvm.scripts.util

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required

fun CliktCommand.projectRoot() = option(envvar = "PROJECT_ROOT").required()
