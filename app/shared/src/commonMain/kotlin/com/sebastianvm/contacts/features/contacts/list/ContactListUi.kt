package com.sebastianvm.contacts.features.contacts.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.Ui
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject

@CircuitInject(ContactListScreen::class, AppScope::class)
@Inject
internal class ContactListUi : Ui<ContactListState, ContactListUiEvent>() {
    @Composable
    override fun Content(
        state: ContactListState,
        handleEvent: EventHandler<ContactListUiEvent>,
        modifier: Modifier,
    ) {
        Text("Hello, world!")
    }
}
