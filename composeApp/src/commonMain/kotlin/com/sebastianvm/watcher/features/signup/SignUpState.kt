package com.sebastianvm.watcher.features.signup

import androidx.compose.foundation.text.input.TextFieldState
import com.sebastianvm.watcher.mvvm.UiState
import org.jetbrains.compose.resources.StringResource

data class SignUpState(
    val username: TextFieldState,
    val password: TextFieldState,
    val isRequestInFlight: Boolean = false,
    val error: StringResource? = null,
) : UiState
