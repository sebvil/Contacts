package com.sebastianvm.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sebastianvm.contacts.di.AndroidAppGraph
import dev.zacsweers.metro.createGraphFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val graph = createGraphFactory<AndroidAppGraph.Factory>().create(this.applicationContext)
        val app = graph.app
        setContent {
            app()
        }
    }
}
