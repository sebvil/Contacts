package com.sebastianvm.contacts

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sebastianvm.contacts.di.AppGraph
import dev.zacsweers.metro.createGraph

fun main() = application {
    val appGraph = createGraph<AppGraph>()
    val app = appGraph.app
    Window(
        onCloseRequest = ::exitApplication,
        title = "contacts",
    ) {
        app()
    }
}
