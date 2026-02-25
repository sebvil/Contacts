package com.sebastianvm.watcher.navigation

import com.sebastianvm.watcher.mvvm.WatcherScreen
import com.sebastianvm.watcher.util.CommonParcelize

@CommonParcelize data class NavHostScreen(val initialScreen: WatcherScreen) : WatcherScreen
