package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.testutils.baseDbTestSuite
import com.sebastianvm.contacts.testutils.ktorApplicationTest
import com.sebastianvm.contacts.testutils.post
import io.kotest.assertions.asClue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

val LogoutTest by baseDbTestSuite {
    ktorApplicationTest("logout revokes tokens") {
        val refreshToken =
            refreshTokenRepository.generateAndSaveRefreshToken(loggedInUserId()).shouldNotBeNull()
        post(Logout, Logout.Body(refreshToken)).asClue { response ->
            response.status shouldBe HttpStatusCode.OK
            response.body<Logout.Response>() shouldBe Logout.Response
        }
        refreshTokenRepository.verifyAndInvalidateRefreshToken(refreshToken).shouldBeNull()
    }
}
