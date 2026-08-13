package com.sebastianvm.contacts

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sebastianvm.contacts.di.JvmAppGraph
import dev.zacsweers.metro.createGraph

fun main() = application {
    val appGraph = createGraph<JvmAppGraph>()
    val app = appGraph.app
    Window(
        onCloseRequest = ::exitApplication,
        title = "contacts",
    ) {
        app()
    }
}
