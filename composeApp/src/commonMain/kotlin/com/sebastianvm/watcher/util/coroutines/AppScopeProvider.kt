package com.sebastianvm.watcher.util.coroutines

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@ContributesTo(AppScope::class)
interface AppScopeProvider {

    @Suppress("InjectDispatcher")
    @Provides
    @SingleIn(AppScope::class)
    fun provideAppCoroutineScope(): CoroutineScope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())
}
