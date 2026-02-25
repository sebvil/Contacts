package com.sebastianvm.watcher.features.landing

import com.sebastianvm.watcher.mvvm.UserAction

sealed interface LandingUserAction : UserAction {

    data object LoginClicked : LandingUserAction

    data object SignupClicked : LandingUserAction
}
