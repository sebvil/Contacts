package com.sebastianvm.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.features.home.HomeScreen
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.navstack.rememberSaveableNavStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import dev.zacsweers.metro.Inject

@Inject
public class App(private val circuit: Circuit) {

    @Composable
    public operator fun invoke(modifier: Modifier = Modifier) {
        MaterialTheme {
            Column(
                modifier =
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                        .navigationBarsPadding()
                        .imePadding()
                        .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircuitCompositionLocals(circuit) {
                    val navStack = rememberSaveableNavStack(root = HomeScreen)
                    val navigator = rememberCircuitNavigator(navStack) {}
                    NavigableCircuitContent(
                        navigator = navigator,
                        navStack = navStack,
                        decoratorFactory =
                            remember(navigator) {
                                GestureNavigationDecorationFactory()
                            },
                    )
                }
            }
        }
    }
}
