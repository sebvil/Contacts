package com.sebastianvm.contacts.features.base

import com.slack.circuit.runtime.presenter.Presenter as CircuitPresenter

interface Presenter<S : UiState, E : UiEvent> : CircuitPresenter<ScreenState<S, E>>
