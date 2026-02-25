package com.sebastianvm.contacts.features.login

import com.sebastianvm.contacts.mvvm.UserAction

sealed interface LoginUserAction : UserAction {
    data object LoginClicked : LoginUserAction

    data object SignUpInsteadClicked : LoginUserAction
}
