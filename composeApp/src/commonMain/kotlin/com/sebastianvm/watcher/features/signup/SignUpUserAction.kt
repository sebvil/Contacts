package com.sebastianvm.watcher.features.signup

import com.sebastianvm.watcher.mvvm.UserAction

sealed interface SignUpUserAction : UserAction {
    data object SignUpClicked : SignUpUserAction

    data object LoginInsteadClicked : SignUpUserAction
}
