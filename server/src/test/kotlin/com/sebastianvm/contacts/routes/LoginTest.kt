package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.authentication.AuthenticationTestHelpers.verifyToken
import com.sebastianvm.contacts.testutils.TestConstants
import com.sebastianvm.contacts.testutils.baseDbTestSuite
import com.sebastianvm.contacts.testutils.ktorApplicationTest
import com.sebastianvm.contacts.testutils.post
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

val LoginTest by baseDbTestSuite {
    ktorApplicationTest(
        "login returns valid tokens for valid user and password",
        isLoggedIn = false,
    ) {
        userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
        val response =
            post(
                Login,
                Login.Body(
                    username = TestConstants.USERNAME,
                    password = TestConstants.VALID_PASSWORD,
                ),
            )
        response.status shouldBe HttpStatusCode.OK
        val body = response.body<Login.Response>().shouldBeInstanceOf<Login.Response.Success>()
        verifyToken(body.accessToken.token)
    }

    ktorApplicationTest("login returns error for invalid user", isLoggedIn = false) {
        val response =
            post(
                route = Login,
                body =
                    Login.Body(
                        username = TestConstants.USERNAME,
                        password = TestConstants.VALID_PASSWORD,
                    ),
            )

        response.body<Login.Response>() shouldBe Login.Response.InvalidCredentials
        response.status shouldBe HttpStatusCode.Unauthorized
    }

    ktorApplicationTest("login returns error for invalid password", isLoggedIn = false) {
        userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
        val response =
            post(Login, Login.Body(username = TestConstants.USERNAME, password = "bad password"))
        response.body<Login.Response>() shouldBe Login.Response.InvalidCredentials
        response.status shouldBe HttpStatusCode.Unauthorized
    }
}
