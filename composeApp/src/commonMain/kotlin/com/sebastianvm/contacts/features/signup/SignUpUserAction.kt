package com.sebastianvm.contacts.features.signup

import com.sebastianvm.contacts.mvvm.UserAction

sealed interface SignUpUserAction : UserAction {
    data object SignUpClicked : SignUpUserAction

    data object LoginInsteadClicked : SignUpUserAction
}
