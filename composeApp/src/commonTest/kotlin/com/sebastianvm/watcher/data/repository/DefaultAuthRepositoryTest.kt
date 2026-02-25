package com.sebastianvm.watcher.data.repository

import app.cash.turbine.test
import com.sebastianvm.watcher.data.model.LoginState
import com.sebastianvm.watcher.di.TestAppGraph
import com.sebastianvm.watcher.di.graph
import com.sebastianvm.watcher.di.testWithDependencies
import com.sebastianvm.watcher.model.AccessToken
import com.sebastianvm.watcher.networking.enqueueMockResponse
import com.sebastianvm.watcher.routes.Login
import com.sebastianvm.watcher.routes.Logout
import com.sebastianvm.watcher.routes.Register
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalCoroutinesApi::class)
val DefaultAuthRepositoryTest by testSuite {
    context(graph: TestAppGraph)
    fun makeSubject(): DefaultAuthRepository =
        DefaultAuthRepository(
            appScope = graph.testScope,
            tokenRepository = graph.tokenRepository,
            client = graph.httpClient,
        )

    testWithDependencies("loginState changes with accessToken availability") {
        val subject = makeSubject()
        val tokenRepository = graph().tokenRepository
        subject.loginState.test {
            awaitItem() shouldBe null
            awaitItem() shouldBe LoginState.LoggedOut
            tokenRepository.saveToken(ACCESS_TOKEN)
            awaitItem() shouldBe LoginState.LoggedIn
            tokenRepository.clearToken()
            awaitItem() shouldBe LoginState.LoggedOut
        }
    }

    testSuite("register") {
        testWithDependencies(
            "returns success response and saves token on success response from API"
        ) {
            val response: Register.Response = Register.Response.Success(ACCESS_TOKEN)
            enqueueMockResponse(response)

            val subject = makeSubject()
            subject.register(username = "test", password = "test") shouldBe response
            graph().tokenRepository.getToken().first() shouldBe ACCESS_TOKEN
        }

        testWithDependencies(
            "returns error response and does not save token on error response from API"
        ) {
            val response: Register.Response = Register.Response.UserAlreadyExists
            enqueueMockResponse(response)

            val subject = makeSubject()
            subject.register(username = "test", password = "test") shouldBe response
            graph().tokenRepository.getToken().first().shouldBeNull()
        }
    }

    testSuite("logIn") {
        testWithDependencies(
            "returns success response and saves token on success response from API"
        ) {
            val response: Login.Response = Login.Response.Success(ACCESS_TOKEN)
            enqueueMockResponse(response)

            val subject = makeSubject()
            subject.logIn(username = "test", password = "test") shouldBe response
            graph().tokenRepository.getToken().first() shouldBe ACCESS_TOKEN
        }

        testWithDependencies(
            "returns error response and does not save token on error response from API"
        ) {
            val response: Login.Response = Login.Response.InvalidCredentials
            enqueueMockResponse(response)

            val subject = makeSubject()
            subject.logIn(username = "test", password = "test") shouldBe response
            graph().tokenRepository.getToken().first().shouldBeNull()
        }
    }

    testSuite("logout") {
        testWithDependencies("clears token from repository, sends request if logged in") {
            val tokenRepository = graph().tokenRepository
            tokenRepository.saveToken(ACCESS_TOKEN)
            val response = Logout.Response
            enqueueMockResponse(response)

            val subject = makeSubject()
            subject.logOut()
            tokenRepository.getToken().first().shouldBeNull()
            graph().mockEngine.responseHistory.shouldNotBeEmpty()
        }

        testWithDependencies("does not send request if logged out") {
            val response = Logout.Response
            enqueueMockResponse(response)

            val subject = makeSubject()
            subject.logOut()
            graph().mockEngine.responseHistory.shouldBeEmpty()
        }
    }
}

private val ACCESS_TOKEN = AccessToken("token", 1.minutes, "refresh-token")
