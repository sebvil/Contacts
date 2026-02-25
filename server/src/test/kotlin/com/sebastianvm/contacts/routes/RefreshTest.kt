package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.authentication.AuthenticationTestHelpers.verifyToken
import com.sebastianvm.contacts.data.DatabaseRefreshTokenRepository
import com.sebastianvm.contacts.testutils.TestConstants
import com.sebastianvm.contacts.testutils.baseDbTestSuite
import com.sebastianvm.contacts.testutils.ktorApplicationTest
import com.sebastianvm.contacts.testutils.post
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlin.time.Duration.Companion.days

val RefreshTest by baseDbTestSuite {
    ktorApplicationTest(
        "Refresh returns new access token with valid refresh token",
        isLoggedIn = false,
    ) {
        val userId =
            userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)!!
        val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
        val response = post(Refresh, Refresh.Body(refreshToken = refreshToken))
        val body = response.body<Refresh.Response>().shouldBeInstanceOf<Refresh.Response.Success>()
        // make sure a new refresh token was generated
        body.accessToken.refreshToken shouldNotBe refreshToken
        verifyToken(body.accessToken.token)
    }

    ktorApplicationTest("Refresh returns error response when token is reused", isLoggedIn = false) {
        val userId =
            userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)!!
        val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
        // first request
        post(Refresh, Refresh.Body(refreshToken = refreshToken))
        // second request
        val response = post(Refresh, Refresh.Body(refreshToken = refreshToken))
        response.body<Refresh.Response>() shouldBe Refresh.Response.InvalidRefreshToken
        response.status shouldBe HttpStatusCode.Unauthorized
    }

    ktorApplicationTest(
        "Refresh returns error response when refresh token does not exist",
        isLoggedIn = false,
    ) {
        val response = post(Refresh, Refresh.Body(refreshToken = "token"))
        response.body<Refresh.Response>() shouldBe Refresh.Response.InvalidRefreshToken
        response.status shouldBe HttpStatusCode.Unauthorized
    }

    ktorApplicationTest(
        "Refresh returns error response when refresh token is expired",
        isLoggedIn = false,
    ) {
        val userId =
            userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)!!
        val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
        clock.currentTime += DatabaseRefreshTokenRepository.EXPIRATION_TIME + 1.days
        val response = post(Refresh, Refresh.Body(refreshToken = refreshToken))
        response.body<Refresh.Response>() shouldBe Refresh.Response.InvalidRefreshToken
        response.status shouldBe HttpStatusCode.Unauthorized
    }
}
