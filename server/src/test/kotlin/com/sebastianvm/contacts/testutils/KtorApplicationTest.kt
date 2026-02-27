package com.sebastianvm.contacts.testutils

import com.sebastianvm.contacts.authentication.AuthenticationTestHelpers.testRoute
import com.sebastianvm.contacts.authentication.configureAuthentication
import com.sebastianvm.contacts.configureExternalPlugins
import com.sebastianvm.contacts.routes.ContactsRoute
import com.sebastianvm.contacts.routes.PostBody
import com.sebastianvm.contacts.routes.configureRoutes
import de.infix.testBalloon.framework.core.Test
import de.infix.testBalloon.framework.core.TestFixture
import de.infix.testBalloon.framework.shared.TestRegistering
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

context(builder: ApplicationTestBuilder)
suspend inline fun <reified R : ContactsRoute<*>, reified B : PostBody<R>> post(
    route: R,
    body: B,
): HttpResponse {
    return builder.client.post(route) {
        contentType(ContentType.Application.Json)
        setBody(body)
    }
}

context(builder: ApplicationTestBuilder)
suspend inline fun <reified R : ContactsRoute<*>> get(route: R): HttpResponse {
    return builder.client.get(route)
}

@TestRegistering
internal fun TestFixture.Scope<
    suspend TestDependencyContainer.(testExecutionScope: Test.ExecutionScope) -> Unit
>
    .ktorApplicationTest(
    name: String,
    isLoggedIn: Boolean = true,
    action:
        suspend context(ApplicationTestBuilder)
        TestDependencyContainer.(Test.ExecutionScope) -> Unit,
) =
    test(name) { scope ->
        val authToken =
            if (isLoggedIn) {
                userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
                val response =
                    userRepository.login(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
                        as Success
                response.accessToken
            } else {
                null
            }
        testApplication {
            application {
                configureExternalPlugins()
                configureAuthentication(algorithm)
                configureRoutes(
                    userRepository = userRepository,
                    refreshTokenRepository = refreshTokenRepository,
                    jwtProvider = jwtProvider,
                )
                this.routing { testRoute() }
            }

            environment { config = applicationConfig }

            client = createClient {
                install(ContentNegotiation) { json() }
                install(Resources)
                if (authToken != null) {
                    defaultRequest { bearerAuth(authToken.token) }
                }
            }
            context(this@testApplication) { action(scope) }
        }
    }
