package com.sebastianvm.contacts.navigation

import androidx.compose.runtime.Composable
import com.sebastianvm.contacts.mvvm.ContactsPresenter
import com.sebastianvm.contacts.mvvm.ScreenState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.navstack.rememberSaveableNavStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

@AssistedInject
class NavHostPresenter(@Assisted private val screen: NavHostScreen) :
    ContactsPresenter<NavHostState, Nothing> {

    @Composable
    override fun present(): ScreenState<NavHostState, Nothing> {
        val navStack = rememberSaveableNavStack(root = screen.initialScreen)
        val navigator = rememberCircuitNavigator(navStack = navStack, onRootPop = {})
        return ScreenState(
            uiState =
                NavHostState(
                    navStack = navStack,
                    navigator = navigator,
                    showTopNavBar = screen.showTopNavBar,
                ),
            handle = {},
        )
    }

    @CircuitInject(NavHostScreen::class, AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: NavHostScreen): NavHostPresenter
    }
}
