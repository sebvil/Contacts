package com.sebastianvm.contacts.di

import dev.zacsweers.metro.createDynamicGraph
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine

fun createTestAppGraph(mockEngine: HttpClientEngine = MockEngine.Queue()) =
    createDynamicGraph<AppGraph>(FakeBindings(engine = mockEngine))
