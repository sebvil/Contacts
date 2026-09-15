@file:Suppress("InjectDispatcher")

package com.sebastianvm.core.corutines

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
@BindingContainer
object DispatcherProvider {

    @Provides fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
