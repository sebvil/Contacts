package com.sebastianvm.watcher.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sebastianvm.watcher.mvvm.Ui
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import dev.zacsweers.metro.AppScope

@CircuitInject(NavHostScreen::class, AppScope::class)
class NavHostUi : Ui<NavHostState, Nothing>() {
    @Composable
    override fun Content(state: NavHostState, handle: (Nothing) -> Unit, modifier: Modifier) {
        NavigableCircuitContent(
            navigator = state.navigator,
            backStack = state.backstack,
            decoratorFactory =
                remember(state.navigator) {
                    GestureNavigationDecorationFactory(onBackInvoked = state.navigator::pop)
                },
        )
    }
}
