package com.sebastianvm.contacts.features.home

import androidx.compose.runtime.Composable
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.StaticPresenter
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
internal class HomePresenter(@Assisted val navigator: Navigator) : StaticPresenter<HomeEvent>() {
    @Composable
    override fun presentEventHandler(): EventHandler<HomeEvent> {
        return {  }
    }

    @CircuitInject(HomeScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): HomePresenter
    }
}
