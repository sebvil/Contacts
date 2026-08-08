package com.sebastianvm.contacts.features.contacts.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.features.base.Presenter
import com.sebastianvm.contacts.features.base.ScreenState
import com.sebastianvm.contacts.features.base.withEventHandler
import com.sebastianvm.contacts.ui.mappers.toContactListItemState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@CircuitInject(ContactListScreen::class, AppScope::class)
@Inject
internal class ContactListPresenter(private val contactsRepository: ContactsRepository) :
    Presenter<ContactListState, ContactListUiEvent> {
    @Composable
    override fun present(): ScreenState<ContactListState, ContactListUiEvent> {
        val state by
            produceRetainedState<ContactListState>(ContactListState.Loading) {
                contactsRepository
                    .getContacts()
                    .map<List<Contact>, ContactListState> { contacts ->
                        ContactListState.Data(contacts.map { it.toContactListItemState() })
                    }
                    .catch { emit(ContactListState.Error) }
                    .collect { value = it }
            }

        return state withEventHandler {}
    }
}
