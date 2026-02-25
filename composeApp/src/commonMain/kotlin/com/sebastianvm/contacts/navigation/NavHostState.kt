package com.sebastianvm.contacts.navigation

import com.sebastianvm.contacts.mvvm.UiState
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.runtime.Navigator

data class NavHostState(val backstack: SaveableBackStack, val navigator: Navigator) : UiState
