package com.sebastianvm.contacts.features.login

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.components.layouts.AuthScreen
import com.sebastianvm.contacts.mvvm.Ui
import com.sebastianvm.contacts.mvvm.util.PreviewScreen
import com.sebastianvm.contacts.mvvm.util.ScreenPreviews
import com.sebastianvm.contacts.ui.previews.LongString
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import org.jetbrains.compose.resources.StringResource
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.dont_have_an_account_sign_up_instead
import contacts.composeapp.generated.resources.invalid_username_or_password
import contacts.composeapp.generated.resources.log_in

@CircuitInject(screen = LoginScreen::class, scope = AppScope::class)
class LoginUi : Ui<LoginState, LoginUserAction>() {

    @Composable
    override fun Content(state: LoginState, handle: (LoginUserAction) -> Unit, modifier: Modifier) {
        AuthScreen(
            state =
                AuthScreen.State(
                    authActionText = Res.string.log_in,
                    switchAuthMethodText = Res.string.dont_have_an_account_sign_up_instead,
                    username = state.username,
                    password = state.password,
                    isRequestInFlight = state.isRequestInFlight,
                    error = state.error,
                ),
            onAuthButtonClick = { handle(LoginUserAction.LoginClicked) },
            onSwitchAuthMethodClick = { handle(LoginUserAction.SignUpInsteadClicked) },
            modifier = modifier,
        )
    }
}

internal class LoginPreviews : ScreenPreviews<LoginState>() {
    override val ui: Ui<LoginState, *>
        get() = LoginUi()

    @PreviewScreen
    @Composable
    internal fun DefaultState() {
        Preview(state = makeState())
    }

    @PreviewScreen
    @Composable
    internal fun WithUserName() {
        Preview(state = makeState(username = "testUser"))
    }

    @PreviewScreen
    @Composable
    internal fun WithLongUsername() {
        Preview(state = makeState(username = LongString))
    }

    @PreviewScreen
    @Composable
    internal fun WithPassword() {
        Preview(state = makeState(password = "password"))
    }

    @PreviewScreen
    @Composable
    internal fun WithLongPassword() {
        Preview(state = makeState(password = LongString))
    }

    @PreviewScreen
    @Composable
    internal fun WithError() {
        Preview(state = makeState(error = Res.string.invalid_username_or_password))
    }

    @PreviewScreen
    @Composable
    internal fun WithRequestInFlight() {
        Preview(state = makeState(isRequestInFlight = true))
    }

    private fun makeState(
        username: String = "",
        password: String = "",
        isRequestInFlight: Boolean = false,
        error: StringResource? = null,
    ) =
        LoginState(
            username = TextFieldState(username),
            password = TextFieldState(password),
            isRequestInFlight = isRequestInFlight,
            error = error,
        )
}
