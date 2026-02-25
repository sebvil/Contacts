package com.sebastianvm.watcher.networking

import com.sebastianvm.watcher.di.TestAppGraph
import com.sebastianvm.watcher.routes.NetworkResponse
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.headers
import kotlinx.serialization.json.Json

@ContributesTo(AppScope::class, replaces = [HttpClientEngineProvider::class])
interface MockClientEngineProvider {

    val mockEngine: MockEngine.Queue

    @SingleIn(AppScope::class)
    @Provides
    fun provideMockEngine(): MockEngine.Queue = MockEngine.Queue()

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientEngine(@Provides mockEngine: MockEngine.Queue): HttpClientEngine =
        mockEngine
}

context(graph: TestAppGraph)
inline fun <reified T : NetworkResponse> enqueueMockResponse(response: T) {
    graph.mockEngine.enqueue {
        respond(
            content = Json.encodeToString(response),
            status = response.code,
            headers = headers { set("Content-Type", "application/json") },
        )
    }
}
