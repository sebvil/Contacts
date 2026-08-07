package com.sebastianvm.contacts.di

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine

@BindingContainer
class FakeBindings(private val engine: HttpClientEngine = MockEngine.Queue()) {

    @Provides fun provideHttpClientEngine(): HttpClientEngine = engine
}
