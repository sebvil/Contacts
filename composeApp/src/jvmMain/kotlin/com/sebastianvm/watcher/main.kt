package com.sebastianvm.watcher

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sebastianvm.watcher.di.JvmAppGraph
import dev.zacsweers.metro.createGraph

fun main() = application {
    val graph = createGraph<JvmAppGraph>()
    Window(onCloseRequest = ::exitApplication, title = "Watcher") { graph.watcherApp() }
}
