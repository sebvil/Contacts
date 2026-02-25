package com.sebastianvm.contacts.networking

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

@ContributesTo(AppScope::class)
interface HttpClientEngineProvider {
    @Provides
    fun provideHttpClientEngine(): HttpClientEngine {
        return OkHttp.create()
    }
}
