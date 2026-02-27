package com.sebastianvm.contacts.features.login

import com.sebastianvm.contacts.di.TestAppGraph
import com.sebastianvm.contacts.di.graph
import com.sebastianvm.contacts.di.testWithDependencies
import com.sebastianvm.contacts.features.landing.LandingScreen
import com.sebastianvm.contacts.features.signup.SignUpScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.invalid_username_or_password
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe

val LoginPresenterTest by testSuite {
    context(graph: TestAppGraph)
    fun makeSubject(navigator: Navigator) = LoginPresenter(graph.authRepository, navigator)

    testFixture { FakeNavigator(LandingScreen, LoginScreen) } asParameterForEach
        {
            testSuite("handle") {
                testSuite("LoginClicked") {
                    testWithDependencies("updates loading state when request is successful") {
                        navigator ->
                        makeSubject(navigator).test {
                            graph().authRepository.addUser("", "")
                            val state = awaitItem()
                            state.uiState.isRequestInFlight shouldBe false
                            state.handle(LoginClicked)
                            awaitItem().uiState.isRequestInFlight shouldBe true
                            awaitItem().uiState.isRequestInFlight shouldBe false
                        }
                    }

                    testWithDependencies("updates loading and error state when request fails") {
                        navigator ->
                        makeSubject(navigator).test {
                            val state = awaitItem()
                            state.uiState.isRequestInFlight shouldBe false
                            state.handle(LoginClicked)
                            awaitItem().uiState.isRequestInFlight shouldBe true
                            awaitItem().uiState.isRequestInFlight shouldBe false
                            awaitItem().uiState.error shouldBe
                                Res.string.invalid_username_or_password
                        }
                    }
                }
            }

            testWithDependencies(
                "SignUpInsteadClicked pops backstack and navigates to SignUpScreen"
            ) { navigator ->
                makeSubject(navigator).test {
                    val state = awaitItem()
                    state.handle(SignUpInsteadClicked)
                    navigator.awaitNextScreen() shouldBe SignUpScreen
                    navigator.awaitPop().asClue { it.poppedScreen shouldBe LoginScreen }
                }
            }
        }
}
