package com.sebastianvm.contacts.features.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

abstract class StaticUi<E : UiEvent> : Ui<UiState.None, E>() {

    @Composable
    final override fun Content(
        state: UiState.None,
        handleEvent: EventHandler<E>,
        modifier: Modifier,
    ) {
        Content(handleEvent = handleEvent, modifier = modifier)
    }

    @Composable protected abstract fun Content(handleEvent: EventHandler<E>, modifier: Modifier)
}
