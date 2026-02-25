package com.sebastianvm.watcher.features.landing

import androidx.compose.runtime.Composable
import com.sebastianvm.watcher.features.login.LoginScreen
import com.sebastianvm.watcher.features.signup.SignUpScreen
import com.sebastianvm.watcher.mvvm.ScreenState
import com.sebastianvm.watcher.mvvm.WatcherPresenter
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import dev.zacsweers.metro.AppScope

@CircuitInject(LandingScreen::class, AppScope::class)
class LandingPresenter(private val navigator: Navigator) :
    WatcherPresenter<LandingState, LandingUserAction> {
    @Composable
    override fun present(): ScreenState<LandingState, LandingUserAction> {
        return ScreenState(
            LandingState,
            { action ->
                when (action) {
                    is LoginClicked -> {
                        navigator.goTo(LoginScreen)
                    }

                    is SignupClicked -> {
                        navigator.goTo(SignUpScreen)
                    }
                }
            },
        )
    }
}
