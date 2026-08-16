package com.sebastianvm.contacts.features.contacts.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.features.base.Presenter
import com.sebastianvm.contacts.features.base.ScreenState
import com.sebastianvm.contacts.features.base.withEventHandler
import com.sebastianvm.contacts.features.contacts.details.ContactDetailsScreen
import com.sebastianvm.contacts.ui.mappers.toContactListItemState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AssistedInject
class ContactListPresenter(
    @Assisted private val navigator: Navigator,
    private val contactsRepository: suspend () -> ContactsRepository,
) : Presenter<ContactListState, ContactListUiEvent> {

    private companion object {
        val SHOW_LOADING_DELAY = 200.milliseconds
        val MIN_LOADING_DURATION = 200.milliseconds
    }

    @Composable
    override fun present(): ScreenState<ContactListState, ContactListUiEvent> {
        var retryCount by remember { mutableIntStateOf(0) }
        val state by
            produceRetainedState<ContactListState>(
                initialValue = ContactListState.Idle,
                key1 = retryCount,
            ) {
                value = ContactListState.Idle
                // Don't show a loading spinner for requests that resolve quickly, and once shown,
                // don't let it disappear so fast that it just reads as a flicker.
                val producerScope = this
                var minLoadingDurationJob: Job? = null
                val showLoadingJob = launch {
                    delay(SHOW_LOADING_DELAY)
                    value = ContactListState.Loading
                    // Launched from producerScope, not this coroutine, so cancelling showLoadingJob
                    // below (once data arrives) doesn't cascade into cancelling this job too - a
                    // launch here would make it a child of showLoadingJob, which cancel() would
                    // kill.
                    minLoadingDurationJob = producerScope.launch { delay(MIN_LOADING_DURATION) }
                }
                contactsRepository()
                    .getContacts()
                    .map<List<Contact>, ContactListState> { contacts ->
                        ContactListState.Data(contacts.map { it.toContactListItemState() })
                    }
                    .catch {
                        println(it)
                        emit(ContactListState.Error)
                    }
                    .collect { newState ->
                        showLoadingJob.cancel()
                        minLoadingDurationJob?.join()
                        value = newState
                    }
            }

        return state withEventHandler
            { event ->
                when (event) {
                    is ContactListUiEvent.TryAgain -> {
                        retryCount++
                    }

                    is ContactListUiEvent.ContactClicked -> {
                        navigator.goTo(ContactDetailsScreen(event.contactId))
                    }
                }
            }
    }

    @CircuitInject(ContactListScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): ContactListPresenter
    }
}
