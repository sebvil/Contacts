package com.sebastianvm.watcher.navigation

import com.sebastianvm.watcher.mvvm.UiState
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.runtime.Navigator

data class NavHostState(val backstack: SaveableBackStack, val navigator: Navigator) : UiState
