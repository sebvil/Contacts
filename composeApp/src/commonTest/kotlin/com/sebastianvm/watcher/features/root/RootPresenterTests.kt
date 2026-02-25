package com.sebastianvm.watcher.features.root

import com.sebastianvm.watcher.data.model.LoginState
import com.sebastianvm.watcher.di.TestAppGraph
import com.sebastianvm.watcher.di.graph
import com.sebastianvm.watcher.di.testWithDependencies
import com.sebastianvm.watcher.features.home.HomeScreen
import com.sebastianvm.watcher.features.landing.LandingScreen
import com.sebastianvm.watcher.navigation.NavHostScreen
import com.slack.circuit.test.test
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
val RootPresenterTests by testSuite {
    context(graph: TestAppGraph)
    fun makeSubject() = RootPresenter(graph.authRepository)

    testWithDependencies("screen changes with loginState") {
        val authRepository = graph().authRepository
        makeSubject().test {
            awaitItem().uiState.screen shouldBe null
            authRepository.loginState.value = LoginState.LoggedOut
            awaitItem().uiState.screen shouldBe NavHostScreen(LandingScreen)
            authRepository.loginState.value = LoginState.LoggedIn
            awaitItem().uiState.screen shouldBe NavHostScreen(HomeScreen)
        }
    }
}
