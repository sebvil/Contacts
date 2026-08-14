package com.sebastianvm.contacts.features.contacts.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
class CreateContactPresenter(@Assisted private val navigator: Navigator) :
    Presenter<CreateContactState, CreateContactUiEvent> {

    @Composable
    override fun present(): ScreenState<CreateContactState, CreateContactUiEvent> {
        var name by remember { mutableStateOf("") }
        val isSaving by remember { mutableStateOf(false) }
        val state =
            CreateContactState(
                name = name,
                isSaving = isSaving,
            )

        return state withEventHandler
            { event ->
                when (event) {
                    is CreateContactUiEvent.NameChanged -> name = event.name
                    CreateContactUiEvent.SaveContact -> TODO()
                    CreateContactUiEvent.BackClicked -> navigator.pop()
                }
            }
    }

    @CircuitInject(CreateContactScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): CreateContactPresenter
    }
}
