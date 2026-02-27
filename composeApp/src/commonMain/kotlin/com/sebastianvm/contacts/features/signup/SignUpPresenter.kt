package com.sebastianvm.contacts.features.signup

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.sebastianvm.contacts.data.repository.AuthRepository
import com.sebastianvm.contacts.features.login.LoginScreen
import com.sebastianvm.contacts.mvvm.ContactsPresenter
import com.sebastianvm.contacts.mvvm.ScreenState
import com.sebastianvm.contacts.routes.Register
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.username_already_taken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

@AssistedInject
class SignUpPresenter(
    private val authRepository: AuthRepository,
    @Assisted private val navigator: Navigator,
) : ContactsPresenter<SignUpState, SignUpUserAction> {
    @Composable
    override fun present(): ScreenState<SignUpState, SignUpUserAction> {
        val username = rememberTextFieldState()
        val password = rememberTextFieldState()
        var isRequestInFlight by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        var error by remember { mutableStateOf<StringResource?>(null) }
        return ScreenState(
            SignUpState(
                username = username,
                password = password,
                isRequestInFlight = isRequestInFlight,
                error = error,
            )
        ) { action ->
            when (action) {
                LoginInsteadClicked -> {
                    navigator.pop()
                    navigator.goTo(LoginScreen)
                }
                SignUpClicked -> {
                    scope.launch {
                        isRequestInFlight = true
                        val result =
                            authRepository.register(
                                username.text.toString(),
                                password.text.toString(),
                            )
                        isRequestInFlight = false
                        if (result is Register.Response.UserAlreadyExists) {
                            error = Res.string.username_already_taken
                        }
                    }
                }
            }
        }
    }

    @CircuitInject(screen = SignUpScreen::class, scope = AppScope::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SignUpPresenter
    }
}
