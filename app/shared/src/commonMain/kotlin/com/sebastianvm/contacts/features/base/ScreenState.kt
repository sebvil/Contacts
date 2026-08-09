package com.sebastianvm.contacts.features.base

import com.slack.circuit.runtime.CircuitUiState

data class ScreenState<S : UiState, E : UiEvent>(
    val state: S,
    val handleEvent: EventHandler<E>,
) : CircuitUiState

infix fun <S : UiState, E : UiEvent> S.withEventHandler(
    eventHandler: EventHandler<E>
): ScreenState<S, E> = ScreenState(state = this, handleEvent = eventHandler)
