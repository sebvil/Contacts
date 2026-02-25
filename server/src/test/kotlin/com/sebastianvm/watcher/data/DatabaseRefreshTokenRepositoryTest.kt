package com.sebastianvm.watcher.data

import com.sebastianvm.watcher.db.model.toRefreshToken
import com.sebastianvm.watcher.db.schema.RefreshTokens
import com.sebastianvm.watcher.testutils.baseDbTestSuite
import com.sebastianvm.watcher.testutils.testWithUser
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.r2dbc.mapLazy
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

val DatabaseRefreshTokenRepositoryTest by baseDbTestSuite {
    testSuite("generateAndSaveRefreshToken") {
        testWithUser("generates and saves a valid refresh token") { _, userId ->
            val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
            refreshToken.shouldNotBeNull()
            suspendTransaction {
                val tokens = RefreshTokens.selectAll().toList().map { it.toRefreshToken() }
                tokens.size shouldBe 1
                val token = tokens.first()
                token.token shouldBe tokenHasher.hash(refreshToken)
                // Guard to make sure hasher is actually hashing
                token.token shouldNotBe refreshToken
                token.userId shouldBe userId
                token.expiresAt shouldBe
                    clock.now() + DatabaseRefreshTokenRepository.EXPIRATION_TIME
                token.revokedAt.shouldBeNull()
            }
            refreshTokenRepository.verifyAndInvalidateRefreshToken(refreshToken).shouldNotBeNull()
        }

        testSuite("generates multiple refresh tokens for the same user") {
            testWithUser("with different family ids") { _, userId ->
                val firstRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                val secondRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                firstRefreshToken shouldNotBe secondRefreshToken
                val tokens = suspendTransaction {
                    RefreshTokens.selectAll().mapLazy { it.toRefreshToken() }.toList()
                }

                tokens.size shouldBe 2
                tokens[0].familyId shouldNotBe tokens[1].familyId

                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(firstRefreshToken)
                    .shouldNotBeNull()
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(secondRefreshToken)
                    .shouldNotBeNull()
            }

            testWithUser("rotation preserves family id") { _, userId ->
                val firstRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                val secondRefreshToken =
                    refreshTokenRepository
                        .verifyAndInvalidateRefreshToken(firstRefreshToken)
                        .shouldNotBeNull()
                        .token
                val tokens = suspendTransaction {
                    RefreshTokens.selectAll().mapLazy { it.toRefreshToken() }.toList()
                }

                tokens.size shouldBe 2
                tokens[0].familyId shouldBe tokens[1].familyId

                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(secondRefreshToken)
                    .shouldNotBeNull()
            }
        }
    }

    testSuite("verifyAndInvalidateRefreshToken") {
        testWithUser("returns a valid token") { _, userId ->
            val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
            refreshToken.shouldNotBeNull()
            val newRefreshToken =
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(refreshToken)
                    .shouldNotBeNull()
                    .token
            refreshTokenRepository
                .verifyAndInvalidateRefreshToken(newRefreshToken)
                .shouldNotBeNull()
        }
        listOf(1.days, 15.days, 29.days, 30.days - 1.seconds).forEach { duration ->
            testWithUser("returns new token for valid token after $duration") { _, userId ->
                val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
                refreshToken.shouldNotBeNull()
                clock.currentTime += duration
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(refreshToken)
                    .shouldNotBeNull()
            }
        }

        testSuite("returns null") {
            testWithUser("for valid token after 30 days") { _, userId ->
                val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
                refreshToken.shouldNotBeNull()
                clock.currentTime += 30.days
                refreshTokenRepository.verifyAndInvalidateRefreshToken(refreshToken).shouldBeNull()
            }

            testWithUser("for reused token") { _, userId ->
                val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
                refreshToken.shouldNotBeNull()
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(refreshToken)
                    .shouldNotBeNull()
                refreshTokenRepository.verifyAndInvalidateRefreshToken(refreshToken).shouldBeNull()
            }

            test("for token that doesn't exist") {
                refreshTokenRepository.verifyAndInvalidateRefreshToken("bad token").shouldBeNull()
            }
        }

        testSuite("revokes all refresh tokens in the same family upon reuse") {
            testWithUser("of non-expired token") { _, userId ->
                val firstRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                val secondRefreshToken =
                    refreshTokenRepository
                        .verifyAndInvalidateRefreshToken(firstRefreshToken)
                        .shouldNotBeNull()
                        .token
                // Replay first token - triggers family revocation
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(firstRefreshToken)
                    .shouldBeNull()
                // Second token should be revoked as part of the family
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(secondRefreshToken)
                    .shouldBeNull()
            }

            testWithUser("ofexpired token") { _, userId ->
                val firstRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                clock.currentTime += DatabaseRefreshTokenRepository.EXPIRATION_TIME / 2
                val secondRefreshToken =
                    refreshTokenRepository
                        .verifyAndInvalidateRefreshToken(firstRefreshToken)
                        .shouldNotBeNull()
                        .token
                // Only expire the first token
                clock.currentTime += DatabaseRefreshTokenRepository.EXPIRATION_TIME / 2

                // Replay the first token after expiration - still triggers family revocation
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(firstRefreshToken)
                    .shouldBeNull()
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(secondRefreshToken)
                    .shouldBeNull()
            }
        }

        testSuite("does not revoke tokens of different families") {
            testWithUser("of non-expired token") { _, userId ->
                val firstRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                val secondRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(firstRefreshToken)
                    .shouldNotBeNull()
                // Replay first token - triggers family revocation
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(firstRefreshToken)
                    .shouldBeNull()
                // Second token should not be revoked as part of the family
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(secondRefreshToken)
                    .shouldNotBeNull()
            }

            testWithUser("of expired token") { _, userId ->
                val firstRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(firstRefreshToken)
                    .shouldNotBeNull()
                clock.currentTime += DatabaseRefreshTokenRepository.EXPIRATION_TIME
                val secondRefreshToken =
                    refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
                // Replay the first token after expiration - still triggers family revocation
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(firstRefreshToken)
                    .shouldBeNull()
                refreshTokenRepository
                    .verifyAndInvalidateRefreshToken(secondRefreshToken)
                    .shouldNotBeNull()
            }
        }

        testWithUser("logout revokes tokens in the same family only") { _, userId ->
            val firstRefreshToken =
                refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
            val secondRefreshToken =
                refreshTokenRepository.generateAndSaveRefreshToken(userId).shouldNotBeNull()
            refreshTokenRepository.logout(firstRefreshToken)
            refreshTokenRepository.verifyAndInvalidateRefreshToken(firstRefreshToken).shouldBeNull()
            refreshTokenRepository
                .verifyAndInvalidateRefreshToken(secondRefreshToken)
                .shouldNotBeNull()
        }
    }
}
