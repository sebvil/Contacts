package com.sebastianvm.contacts.features.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.ui.Ui as CircuitUi

public abstract class Ui<S : UiState, E : UiEvent> : CircuitUi<ScreenState<S, E>> {
    @Composable
    final override fun Content(state: ScreenState<S, E>, modifier: Modifier) {
        Content(state = state.state, handleEvent = state.handleEvent, modifier = modifier)
    }

    @Composable
    protected abstract fun Content(state: S, handleEvent: EventHandler<E>, modifier: Modifier)
}
