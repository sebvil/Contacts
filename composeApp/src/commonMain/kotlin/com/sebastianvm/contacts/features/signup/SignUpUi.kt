package com.sebastianvm.contacts.features.signup

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
import contacts.composeapp.generated.resources.already_have_an_account_log_in_instead
import contacts.composeapp.generated.resources.sign_up
import contacts.composeapp.generated.resources.username_already_taken

@CircuitInject(screen = SignUpScreen::class, scope = AppScope::class)
class SignUpUi : Ui<SignUpState, SignUpUserAction>() {

    @Composable
    override fun Content(
        state: SignUpState,
        handle: (SignUpUserAction) -> Unit,
        modifier: Modifier,
    ) {
        AuthScreen(
            state =
                AuthScreen.State(
                    authActionText = Res.string.sign_up,
                    switchAuthMethodText = Res.string.already_have_an_account_log_in_instead,
                    username = state.username,
                    password = state.password,
                    isRequestInFlight = state.isRequestInFlight,
                    error = state.error,
                ),
            onAuthButtonClick = { handle(SignUpClicked) },
            onSwitchAuthMethodClick = { handle(LoginInsteadClicked) },
            modifier = modifier,
        )
    }
}

internal class SignUpPreviews : ScreenPreviews<SignUpState>() {
    override val ui: Ui<SignUpState, *>
        get() = SignUpUi()

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
        Preview(state = makeState(error = Res.string.username_already_taken))
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
        SignUpState(
            username = TextFieldState(username),
            password = TextFieldState(password),
            isRequestInFlight = isRequestInFlight,
            error = error,
        )
}
