package com.sebastianvm.contacts.features.signup

import com.sebastianvm.contacts.di.TestAppGraph
import com.sebastianvm.contacts.di.graph
import com.sebastianvm.contacts.di.testWithDependencies
import com.sebastianvm.contacts.features.landing.LandingScreen
import com.sebastianvm.contacts.features.login.LoginScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.username_already_taken
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe

val SignUpPresenterTest by testSuite {
    context(graph: TestAppGraph)
    fun makeSubject(navigator: Navigator): SignUpPresenter {
        return SignUpPresenter(authRepository = graph.authRepository, navigator = navigator)
    }

    testFixture { FakeNavigator(LandingScreen, SignUpScreen) } asParameterForEach
        {
            testSuite("handle") {
                testSuite("SignUpClicked") {
                    testWithDependencies("updates loading state when request is successful") {
                        navigator ->
                        makeSubject(navigator).test {
                            val state = awaitItem()
                            state.uiState.isRequestInFlight shouldBe false
                            state.handle(SignUpClicked)
                            awaitItem().uiState.isRequestInFlight shouldBe true
                            awaitItem().uiState.isRequestInFlight shouldBe false
                        }
                    }

                    testWithDependencies("updates loading and error state when request fails") {
                        navigator ->
                        graph().authRepository.addUser("", "")
                        makeSubject(navigator).test {
                            val state = awaitItem()
                            state.uiState.isRequestInFlight shouldBe false
                            state.handle(SignUpClicked)
                            awaitItem().uiState.isRequestInFlight shouldBe true
                            awaitItem().uiState.isRequestInFlight shouldBe false
                            awaitItem().uiState.error shouldBe Res.string.username_already_taken
                        }
                    }
                }

                testWithDependencies(
                    "LogInInsteadClicked pops backstack and navigates to login screen"
                ) { navigator ->
                    makeSubject(navigator).test {
                        val state = awaitItem()
                        state.handle(LoginInsteadClicked)
                        navigator.awaitNextScreen() shouldBe LoginScreen
                        navigator.awaitPop().asClue { it.poppedScreen shouldBe SignUpScreen }
                    }
                }
            }
        }
}
