package com.sebastianvm.contacts.designsys.components.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.icons.Eye
import com.sebastianvm.contacts.designsys.icons.EyeOff
import com.sebastianvm.contacts.designsys.icons.Icons
import com.sebastianvm.contacts.designsys.theme.Spacing
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.hide_password
import contacts.composeapp.generated.resources.password_noun
import contacts.composeapp.generated.resources.show_password
import contacts.composeapp.generated.resources.username_noun

object AuthScreen {

    data class State(
        val authActionText: StringResource,
        val switchAuthMethodText: StringResource,
        val username: TextFieldState,
        val password: TextFieldState,
        val isRequestInFlight: Boolean = false,
        val error: StringResource? = null,
    )

    @Composable
    operator fun invoke(
        state: State,
        onAuthButtonClick: () -> Unit,
        onSwitchAuthMethodClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Scaffold(modifier = modifier) { paddingValues ->
            Column(
                modifier =
                    Modifier.padding(paddingValues)
                        .padding(horizontal = Spacing.Sm16)
                        .fillMaxSize(),
                verticalArrangement =
                    Arrangement.spacedBy(Spacing.Sm8, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(state.authActionText),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    state = state.username,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(Res.string.username_noun)) },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    isError = state.error != null,
                )

                PasswordField(state.password, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = onAuthButtonClick,
                    enabled = !state.isRequestInFlight,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(state.authActionText))
                }
                TextButton(onClick = onSwitchAuthMethodClick, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(state.switchAuthMethodText))
                }
            }
        }
    }

    @Composable
    private fun PasswordField(password: TextFieldState, modifier: Modifier = Modifier) {
        val isPasswordVisible = remember { mutableStateOf(false) }
        val icon =
            if (isPasswordVisible.value) {
                Icons.EyeOff
            } else {
                Icons.Eye
            }

        val textObfuscationMode =
            if (isPasswordVisible.value) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            }

        val visibilityButtonContentDescription =
            if (isPasswordVisible.value) {
                stringResource(Res.string.hide_password)
            } else {
                stringResource(Res.string.show_password)
            }
        OutlinedSecureTextField(
            state = password,
            modifier = modifier,
            label = { Text(text = stringResource(Res.string.password_noun)) },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                    Icon(icon, contentDescription = visibilityButtonContentDescription)
                }
            },
            textObfuscationMode = textObfuscationMode,
        )
    }
}
