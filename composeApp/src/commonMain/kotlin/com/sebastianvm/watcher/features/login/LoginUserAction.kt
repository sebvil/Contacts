package com.sebastianvm.watcher.features.login

import com.sebastianvm.watcher.mvvm.UserAction

sealed interface LoginUserAction : UserAction {
    data object LoginClicked : LoginUserAction

    data object SignUpInsteadClicked : LoginUserAction
}
