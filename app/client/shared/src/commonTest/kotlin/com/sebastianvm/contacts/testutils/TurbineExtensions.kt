package com.sebastianvm.contacts.testutils

import com.sebastianvm.contacts.features.base.ScreenState
import com.sebastianvm.contacts.features.base.UiState
import com.slack.circuit.test.CircuitReceiveTurbine

suspend fun <S : UiState> CircuitReceiveTurbine<out ScreenState<S, *>>.awaitState(): S =
    awaitItem().state
