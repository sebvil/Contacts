package com.sebastianvm.watcher

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.watcher.designsys.theme.WatcherTheme
import com.sebastianvm.watcher.features.root.RootScreen
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import dev.zacsweers.metro.Inject

@Inject
class WatcherApp(private val circuit: Circuit) {
    @Composable
    operator fun invoke(modifier: Modifier = Modifier) {
        WatcherTheme {
            Scaffold(modifier = modifier) { paddingValues ->
                CircuitCompositionLocals(circuit) {
                    CircuitContent(screen = RootScreen, modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}
