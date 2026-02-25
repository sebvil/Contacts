package com.sebastianvm.contacts.features.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.sebastianvm.contacts.data.repository.AuthRepository
import com.sebastianvm.contacts.features.home.HomeScreen
import com.sebastianvm.contacts.features.landing.LandingScreen
import com.sebastianvm.contacts.mvvm.ScreenState
import com.sebastianvm.contacts.mvvm.ContactsPresenter
import com.sebastianvm.contacts.navigation.NavHostScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject

@CircuitInject(screen = RootScreen::class, scope = AppScope::class)
@Inject
class RootPresenter(private val auth: AuthRepository) : ContactsPresenter<RootState, Nothing> {
    @Composable
    override fun present(): ScreenState<RootState, Nothing> {
        val loginState = auth.loginState.collectAsState()
        val screen =
            when (loginState.value) {
                LoggedOut -> NavHostScreen(LandingScreen)
                LoggedIn -> NavHostScreen(HomeScreen)
                null -> null
            }
        return ScreenState(RootState(screen = screen)) {}
    }
}
