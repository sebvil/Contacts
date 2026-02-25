package com.sebastianvm.watcher.features.home

import com.sebastianvm.watcher.mvvm.UserAction

sealed interface HomeUserAction : UserAction {
    data object LogOutClicked : HomeUserAction
}
