package com.sebastianvm.contacts.authentication

import com.auth0.jwt.JWT
import com.sebastianvm.contacts.testutils.baseTestSuite
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant
import kotlin.uuid.Uuid

val JwtProviderTest by baseTestSuite {
    testSuite("getToken") {
        test("encodes userId as subject") {
            val userId = Uuid.random()
            val token = jwtProvider.getToken(userId, "refresh-token")
            JWT.decode(token.token).subject shouldBe userId.toString()
        }

        test("uses issuer from config") {
            val token = jwtProvider.getToken(Uuid.random(), "refresh-token")
            JWT.decode(token.token).issuer shouldBe "test"
        }

        test("uses audience from config") {
            val token = jwtProvider.getToken(Uuid.random(), "refresh-token")
            JWT.decode(token.token).audience shouldBe listOf("test")
        }

        test("sets expiresIn to 30 minutes") {
            val token = jwtProvider.getToken(Uuid.random(), "refresh-token")
            token.expiresIn shouldBe 30.minutes
        }

        test("sets issuedAt to current clock time") {
            clock.currentTime = Instant.fromEpochSeconds(1_000_000L)
            val token = jwtProvider.getToken(Uuid.random(), "refresh-token")
            JWT.decode(token.token).issuedAt.time shouldBe clock.currentTime.toEpochMilliseconds()
        }

        test("sets expiresAt to 30 minutes from now") {
            clock.currentTime = Instant.fromEpochSeconds(1_000_000L)
            val token = jwtProvider.getToken(Uuid.random(), "refresh-token")
            JWT.decode(token.token).expiresAt.time shouldBe
                (clock.currentTime + 30.minutes).toEpochMilliseconds()
        }

        test("includes the provided refresh token") {
            val refreshToken = "my-refresh-token"
            val token = jwtProvider.getToken(Uuid.random(), refreshToken)
            token.refreshToken shouldBe refreshToken
        }

        test("generates different tokens for different users") {
            val token1 = jwtProvider.getToken(Uuid.random(), "refresh-1")
            val token2 = jwtProvider.getToken(Uuid.random(), "refresh-2")
            token1.token shouldNotBe token2.token
        }
    }
}
