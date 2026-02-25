package com.sebastianvm.watcher.mvvm

import com.slack.circuit.runtime.presenter.Presenter

interface WatcherPresenter<S : UiState, A : UserAction> : Presenter<ScreenState<S, A>>
