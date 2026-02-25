package com.sebastianvm.watcher.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.ui.Ui

abstract class Ui<S : UiState, A : UserAction> : Ui<ScreenState<S, A>> {

    @Composable
    final override fun Content(state: ScreenState<S, A>, modifier: Modifier) {
        Content(state.uiState, state.handle, modifier)
    }

    @Composable abstract fun Content(state: S, handle: (A) -> Unit, modifier: Modifier = Modifier)
}
