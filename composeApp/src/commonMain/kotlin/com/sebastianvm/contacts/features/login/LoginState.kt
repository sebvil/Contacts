package com.sebastianvm.contacts.features.login

import androidx.compose.foundation.text.input.TextFieldState
import com.sebastianvm.contacts.mvvm.UiState
import org.jetbrains.compose.resources.StringResource

data class LoginState(
    val username: TextFieldState,
    val password: TextFieldState,
    val isRequestInFlight: Boolean = false,
    val error: StringResource? = null,
) : UiState
