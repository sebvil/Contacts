package com.sebastianvm.contacts.features.contacts.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.features.base.Presenter
import com.sebastianvm.contacts.features.base.ScreenState
import com.sebastianvm.contacts.features.base.withEventHandler
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
internal class ContactDetailsPresenter(
    @Assisted private val arguments: ContactDetailsScreen,
    @Assisted private val navigator: Navigator,
    private val contactsRepository: suspend () -> ContactsRepository,
) : Presenter<ContactDetailsState, ContactDetailsUiEvent> {
    @Composable
    override fun present(): ScreenState<ContactDetailsState, ContactDetailsUiEvent> {
        val contact by
            produceState<Contact?>(null) {
                contactsRepository().getContact(arguments.contactId).collect {
                    value = it
                }
            }
        return ContactDetailsState(name = contact?.name.orEmpty()) withEventHandler
            { event ->
                when (event) {
                    ContactDetailsUiEvent.OnBackClicked -> navigator.backward()
                }
            }
    }

    @CircuitInject(ContactDetailsScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(arguments: ContactDetailsScreen, navigator: Navigator): ContactDetailsPresenter
    }
}
