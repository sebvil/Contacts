package com.sebastianvm.watcher.features.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.sebastianvm.watcher.data.repository.AuthRepository
import com.sebastianvm.watcher.features.home.HomeScreen
import com.sebastianvm.watcher.features.landing.LandingScreen
import com.sebastianvm.watcher.mvvm.ScreenState
import com.sebastianvm.watcher.mvvm.WatcherPresenter
import com.sebastianvm.watcher.navigation.NavHostScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject

@CircuitInject(screen = RootScreen::class, scope = AppScope::class)
@Inject
class RootPresenter(private val auth: AuthRepository) : WatcherPresenter<RootState, Nothing> {
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
