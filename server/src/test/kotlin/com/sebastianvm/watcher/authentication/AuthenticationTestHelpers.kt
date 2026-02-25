package com.sebastianvm.watcher.authentication

import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.testing.ApplicationTestBuilder

object AuthenticationTestHelpers {
    fun Route.testRoute() = authenticateRoutes { get(TEST_ROUTE) { call.respond("Logged in") } }

    context(builder: ApplicationTestBuilder)
    suspend fun verifyToken(token: String) {
        val homeResponse =
            builder.client.get(urlString = TEST_ROUTE) { bearerAuth(token) }.body<String>()
        homeResponse shouldBe LOGGED_IN_RESPONSE
    }

    private const val LOGGED_IN_RESPONSE = "Logged in"
    const val TEST_ROUTE = "/test"
}
