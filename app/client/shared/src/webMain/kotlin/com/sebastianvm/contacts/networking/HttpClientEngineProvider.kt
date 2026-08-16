package com.sebastianvm.contacts.networking

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

@ContributesTo(AppScope::class)
interface HttpClientEngineProvider {

    // CIO (used on jvm/android) opens raw sockets via Node's `net` module, which doesn't exist
    // in a browser. Js wraps the browser's `fetch` API instead.
    @Provides
    @SingleIn(AppScope::class)
    private fun provideHttpClientEngine(): HttpClientEngine = Js.create()
}
