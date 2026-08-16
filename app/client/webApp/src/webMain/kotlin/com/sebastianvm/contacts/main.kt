package com.sebastianvm.contacts

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.sebastianvm.contacts.di.WebAppGraph
import dev.zacsweers.metro.createGraph

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val appGraph = createGraph<WebAppGraph>()
    val app = appGraph.app
    ComposeViewport {
        app()
    }
}
