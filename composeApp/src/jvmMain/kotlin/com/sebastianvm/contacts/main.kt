package com.sebastianvm.contacts

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sebastianvm.contacts.di.JvmAppGraph
import dev.zacsweers.metro.createGraph

fun main() = application {
    val graph = createGraph<JvmAppGraph>()
    Window(onCloseRequest = ::exitApplication, title = "Contacts") { graph.contactsApp() }
}
