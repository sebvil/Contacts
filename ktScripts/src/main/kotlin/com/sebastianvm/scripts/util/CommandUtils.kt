package com.sebastianvm.scripts.util

import java.io.File
import java.util.concurrent.TimeUnit

fun String.runCommand(workingDir: File) {
    ProcessBuilder(split(" "))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor(TIMEOUT, TimeUnit.MINUTES)
}

private const val TIMEOUT = 60L
