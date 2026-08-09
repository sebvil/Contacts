package com.sebastianvm.contacts.networking

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json

@ContributesTo(AppScope::class)
interface HttpClientProvider {

    val httpClient: HttpClient

    @Provides
    @SingleIn(AppScope::class)
    private fun provideHttpClientEngine(): HttpClientEngine = CIO.create()

    @Provides
    @SingleIn(AppScope::class)
    private fun provideHttpClient(engine: HttpClientEngine): HttpClient =
        HttpClient(engine) {
            install(Resources)
            install(ContentNegotiation) {
                json()
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "localhost"
                    port = PORT
                }
            }
        }

    private companion object {
        const val PORT = 8080
    }
}
