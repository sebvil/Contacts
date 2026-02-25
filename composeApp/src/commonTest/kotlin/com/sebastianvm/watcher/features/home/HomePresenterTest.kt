package com.sebastianvm.watcher.features.home

import com.sebastianvm.watcher.di.TestAppGraph
import com.sebastianvm.watcher.di.graph
import com.sebastianvm.watcher.di.testWithDependencies
import com.sebastianvm.watcher.networking.enqueueMockResponse
import com.sebastianvm.watcher.routes.Home
import com.slack.circuit.test.test
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val HomePresenterTest by testSuite {
    context(graph: TestAppGraph)
    fun makeSubject(username: String = ""): HomePresenter {
        enqueueMockResponse<Home.Response>(Home.Response.Success(username = username))
        return HomePresenter(client = graph.httpClient, authRepository = graph.authRepository)
    }

    testWithDependencies("Handle LogOutClicked logs out user") {
        graph().authRepository.loginState.value = LoggedIn
        makeSubject().test {
            awaitItem().handle(LogOutClicked)
            graph().authRepository.loginState.value shouldBe LoggedOut
        }
    }

    testWithDependencies("username gets set") {
        val username = "test"
        makeSubject(username = username).test {
            awaitItem().uiState.username shouldBe ""
            awaitItem().uiState.username shouldBe username
        }
    }
}
