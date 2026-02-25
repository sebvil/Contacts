package com.sebastianvm.contacts.features.home

import com.sebastianvm.contacts.mvvm.UserAction

sealed interface HomeUserAction : UserAction {
    data object LogOutClicked : HomeUserAction
}
