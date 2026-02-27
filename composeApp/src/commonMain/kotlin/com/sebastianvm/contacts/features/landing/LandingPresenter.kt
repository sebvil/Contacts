package com.sebastianvm.contacts.features.landing

import androidx.compose.runtime.Composable
import com.sebastianvm.contacts.features.login.LoginScreen
import com.sebastianvm.contacts.features.signup.SignUpScreen
import com.sebastianvm.contacts.mvvm.ContactsPresenter
import com.sebastianvm.contacts.mvvm.ScreenState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import dev.zacsweers.metro.AppScope

@CircuitInject(LandingScreen::class, AppScope::class)
class LandingPresenter(private val navigator: Navigator) :
    ContactsPresenter<LandingState, LandingUserAction> {
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
