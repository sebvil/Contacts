package com.sebastianvm.contacts.features.landing

import com.sebastianvm.contacts.mvvm.UserAction

sealed interface LandingUserAction : UserAction {

    data object LoginClicked : LandingUserAction

    data object SignupClicked : LandingUserAction
}
