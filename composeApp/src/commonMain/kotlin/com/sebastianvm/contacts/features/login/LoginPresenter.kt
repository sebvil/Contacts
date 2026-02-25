package com.sebastianvm.contacts.features.login

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.sebastianvm.contacts.data.repository.AuthRepository
import com.sebastianvm.contacts.features.signup.SignUpScreen
import com.sebastianvm.contacts.mvvm.ScreenState
import com.sebastianvm.contacts.mvvm.ContactsPresenter
import com.sebastianvm.contacts.routes.Login
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.invalid_username_or_password

@AssistedInject
class LoginPresenter(
    private val authRepository: AuthRepository,
    @Assisted private val navigator: Navigator,
) : ContactsPresenter<LoginState, LoginUserAction> {
    @Composable
    override fun present(): ScreenState<LoginState, LoginUserAction> {
        val username = rememberTextFieldState()
        val password = rememberTextFieldState()
        var isRequestInFlight by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        var error by remember { mutableStateOf<StringResource?>(null) }
        return ScreenState(
            LoginState(
                username = username,
                password = password,
                isRequestInFlight = isRequestInFlight,
                error = error,
            )
        ) { action ->
            when (action) {
                SignUpInsteadClicked -> {
                    navigator.pop()
                    navigator.goTo(SignUpScreen)
                }
                LoginClicked -> {
                    scope.launch {
                        isRequestInFlight = true
                        val result =
                            authRepository.logIn(username.text.toString(), password.text.toString())
                        isRequestInFlight = false
                        if (result is Login.Response.InvalidCredentials) {
                            error = Res.string.invalid_username_or_password
                        }
                    }
                }
            }
        }
    }

    @CircuitInject(screen = LoginScreen::class, scope = AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): LoginPresenter
    }
}
