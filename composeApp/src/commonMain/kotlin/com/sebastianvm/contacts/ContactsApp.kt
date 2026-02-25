package com.sebastianvm.contacts

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.theme.ContactsTheme
import com.sebastianvm.contacts.features.root.RootScreen
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import dev.zacsweers.metro.Inject

@Inject
class ContactsApp(private val circuit: Circuit) {
    @Composable
    operator fun invoke(modifier: Modifier = Modifier) {
        ContactsTheme {
            Scaffold(modifier = modifier) { paddingValues ->
                CircuitCompositionLocals(circuit) {
                    CircuitContent(screen = RootScreen, modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}
