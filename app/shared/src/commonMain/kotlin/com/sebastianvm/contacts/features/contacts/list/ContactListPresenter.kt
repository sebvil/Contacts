package com.sebastianvm.contacts.features.contacts.list

import androidx.compose.runtime.Composable
import com.sebastianvm.contacts.features.base.Presenter
import com.sebastianvm.contacts.features.base.ScreenState
import com.sebastianvm.contacts.features.base.withEventHandler
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject

@CircuitInject(ContactListScreen::class, AppScope::class)
@Inject
internal class ContactListPresenter : Presenter<ContactListState, ContactListUiEvent> {
    @Composable
    override fun present(): ScreenState<ContactListState, ContactListUiEvent> {
        return ContactListState.Loading withEventHandler {}
    }
}
