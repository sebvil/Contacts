package com.sebastianvm.contacts.mvvm

import com.slack.circuit.runtime.presenter.Presenter

interface ContactsPresenter<S : UiState, A : UserAction> : Presenter<ScreenState<S, A>>
