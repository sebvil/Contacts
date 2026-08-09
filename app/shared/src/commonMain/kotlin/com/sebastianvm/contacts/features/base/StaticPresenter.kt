package com.sebastianvm.contacts.features.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget

abstract class StaticPresenter<E : UiEvent> : Presenter<UiState.None, E> {

    @Composable
    final override fun present(): ScreenState<UiState.None, E> =
        UiState.None withEventHandler presentEventHandler()

    @Composable
    @ComposableTarget("presenter")
    protected abstract fun presentEventHandler(): EventHandler<E>
}
