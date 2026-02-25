package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.SERVER_PORT
import com.sebastianvm.contacts.data.repository.TokenRepository
import com.sebastianvm.contacts.routes.Refresh
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull

@ContributesTo(AppScope::class)
interface HttpClientProvider {
    @Provides
    fun provideHttpClient(tokenRepository: TokenRepository, engine: HttpClientEngine): HttpClient {
        return makeHttpClient(tokenRepository, engine)
    }
}

fun makeHttpClient(tokenRepository: TokenRepository, engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
        install(Resources)
        install(ContentNegotiation) { json() }
        install(Auth) {
            bearer {
                loadTokens {
                    val token = tokenRepository.getToken().firstOrNull()
                    token?.let {
                        BearerTokens(accessToken = token.token, refreshToken = token.refreshToken)
                    }
                }

                refreshTokens {
                    this.oldTokens?.refreshToken?.let {
                        val newTokens =
                            client.post(Refresh, Refresh.Body(it)) as? Refresh.Response.Success
                        newTokens?.let { tokens ->
                            BearerTokens(
                                accessToken = tokens.accessToken.token,
                                refreshToken = tokens.accessToken.refreshToken,
                            )
                        }
                    }
                }
            }
        }
        defaultRequest {
            host = "localhost"
            port = SERVER_PORT
            contentType(ContentType.Application.Json)
        }
    }
}
