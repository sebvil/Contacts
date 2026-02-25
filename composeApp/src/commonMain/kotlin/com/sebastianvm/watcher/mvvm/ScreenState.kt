package com.sebastianvm.watcher.mvvm

import com.slack.circuit.runtime.CircuitUiState

data class ScreenState<S : UiState, A : UserAction>(val uiState: S, val handle: (A) -> Unit) :
    CircuitUiState
