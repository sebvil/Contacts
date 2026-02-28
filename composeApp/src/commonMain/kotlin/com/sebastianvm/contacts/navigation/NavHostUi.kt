package com.sebastianvm.contacts.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.icons.ArrowLeft
import com.sebastianvm.contacts.designsys.icons.ArrowRight
import com.sebastianvm.contacts.designsys.icons.Icons
import com.sebastianvm.contacts.mvvm.Ui
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.runtime.navigation.canGoBack
import com.slack.circuit.runtime.navigation.canGoForward
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import dev.zacsweers.metro.AppScope

@CircuitInject(NavHostScreen::class, AppScope::class)
class NavHostUi : Ui<NavHostState, Nothing>() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(state: NavHostState, handle: (Nothing) -> Unit, modifier: Modifier) {
        Scaffold(
            modifier = modifier,
            topBar = {
                if (state.showTopNavBar) {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            Row {
                                IconButton(
                                    onClick = { state.navigator.backward() },
                                    enabled = state.navStack.canGoBack,
                                ) {
                                    Icon(Icons.ArrowLeft, contentDescription = "Back")
                                }

                                IconButton(
                                    onClick = { state.navigator.forward() },
                                    enabled = state.navStack.canGoForward,
                                ) {
                                    Icon(Icons.ArrowRight, contentDescription = "Back")
                                }
                            }
                        },
                    )
                }
            },
        ) { padding ->
            NavigableCircuitContent(
                navigator = state.navigator,
                navStack = state.navStack,
                modifier = Modifier.padding(padding),
                decoratorFactory =
                    remember(state.navigator) {
                        GestureNavigationDecorationFactory(onBackInvoked = state.navigator::pop)
                    },
            )
        }
    }
}

expect fun isDesktop(): Boolean
