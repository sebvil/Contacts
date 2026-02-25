package com.sebastianvm.contacts.features.landing

import com.sebastianvm.contacts.features.login.LoginScreen
import com.sebastianvm.contacts.features.signup.SignUpScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val LandingPresenterTest by testSuite {
    fun makeSubject(navigator: Navigator) = LandingPresenter(navigator)

    testFixture { FakeNavigator(LandingScreen) } asParameterForEach
        {
            testSuite("handle") {
                test("SignUpClick navigates to SignUpScreen") { navigator ->
                    val presenter = makeSubject(navigator)
                    presenter.test {
                        val state = awaitItem()
                        state.handle(SignupClicked)
                        navigator.awaitNextScreen() shouldBe SignUpScreen
                    }
                }

                test("LoginClick navigates to LoginScreen") { navigator ->
                    val presenter = makeSubject(navigator)
                    presenter.test {
                        val state = awaitItem()
                        state.handle(LoginClicked)
                        navigator.awaitNextScreen() shouldBe LoginScreen
                    }
                }
            }
        }
}
