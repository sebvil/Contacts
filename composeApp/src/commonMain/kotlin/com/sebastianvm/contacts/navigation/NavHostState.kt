package com.sebastianvm.contacts.navigation

import com.sebastianvm.contacts.mvvm.UiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.navigation.NavStack

data class NavHostState(
    val navStack: NavStack<*>,
    val navigator: Navigator,
    val showTopNavBar: Boolean,
) : UiState
