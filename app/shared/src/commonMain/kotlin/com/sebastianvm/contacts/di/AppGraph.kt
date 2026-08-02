package com.sebastianvm.contacts.di

import com.sebastianvm.contacts.App
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlin.time.Clock

@DependencyGraph(AppScope::class)
@MustUseReturnValues
public interface AppGraph {

    public val app: App

    @Provides
    @SingleIn(AppScope::class)
    private fun provideCircuit(
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>,
    ): Circuit {
        return Circuit.Builder()
            .addPresenterFactories(presenterFactories)
            .addUiFactories(uiFactories)
            .build()
    }

    @Provides private fun provideClock(): Clock = Clock.System
}
