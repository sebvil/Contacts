package com.sebastianvm.watcher.navigation

import androidx.compose.runtime.Composable
import com.sebastianvm.watcher.mvvm.ScreenState
import com.sebastianvm.watcher.mvvm.WatcherPresenter
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class NavHostPresenter(@Assisted private val screen: NavHostScreen) :
    WatcherPresenter<NavHostState, Nothing> {

    @Composable
    override fun present(): ScreenState<NavHostState, Nothing> {
        val backStack = rememberSaveableBackStack(root = screen.initialScreen)
        val navigator = rememberCircuitNavigator(backStack = backStack, onRootPop = {})
        return ScreenState(
            uiState = NavHostState(backstack = backStack, navigator = navigator),
            handle = {},
        )
    }

    @CircuitInject(NavHostScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: NavHostScreen): NavHostPresenter
    }
}
