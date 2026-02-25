package com.sebastianvm.contacts.data

import com.auth0.jwt.JWT
import com.sebastianvm.contacts.db.model.toUser
import com.sebastianvm.contacts.db.schema.Users
import com.sebastianvm.contacts.routes.Login
import com.sebastianvm.contacts.testutils.TestConstants
import com.sebastianvm.contacts.testutils.baseDbTestSuite
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.disable
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.r2dbc.mapLazy
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

val DatabaseUserRepositoryTest by baseDbTestSuite {
    testSuite("createUser") {
        test("adds user to database with hashed password and returns id") {
            val id = userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
            suspendTransaction {
                val users = Users.selectAll().mapLazy { it.toUser() }.toList()
                users.size shouldBe 1
                val user = users.first()
                user.id shouldBe id
                user.username shouldBe TestConstants.USERNAME
                passwordHasher.verify(
                    password = TestConstants.VALID_PASSWORD,
                    hashedPassword = user.password,
                ) shouldBe true
                // This is a guard to ensure the passwordHasher is actually hashing
                user.password shouldNotBe TestConstants.VALID_PASSWORD
            }
        }

        test("with existing username returns null") {
            val existingUserId =
                userRepository
                    .createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
                    .shouldNotBeNull()
            val id = userRepository.createUser(TestConstants.USERNAME, "another password")
            id shouldBe null
            // make sure the user hasn't changed
            suspendTransaction {
                val users = Users.selectAll().mapLazy { it.toUser() }.toList()
                users.size shouldBe 1
                val user = users.first()
                user.id shouldBe existingUserId
                user.username shouldBe TestConstants.USERNAME
                passwordHasher.verify(TestConstants.VALID_PASSWORD, user.password) shouldBe true
            }
        }

        // username validation is not yet implemented
        listOf("", "with space", "with new\nline").forEach { invalidUsername ->
            test(
                """with invalid username "$invalidUsername" returns null""",
                testConfig = TestConfig.disable(),
            ) {
                val id = userRepository.createUser(invalidUsername, TestConstants.VALID_PASSWORD)
                id shouldBe null
            }
        }

        // password validation is not yet implemented
        listOf("", "password", "short", "Password!").forEach { weakPassword ->
            test(
                """with weak password "$weakPassword" returns null""",
                testConfig = TestConfig.disable(),
            ) {
                val id = userRepository.createUser(TestConstants.USERNAME, weakPassword)
                id shouldBe null
            }
        }
    }

    testSuite("login") {
        test("returns success response for valid user and password") {
            val userId =
                userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
            userId.shouldNotBeNull()
            val result = userRepository.login(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
            result.shouldBeInstanceOf<Login.Response.Success>()
            val decoded = JWT.decode(result.accessToken.token)
            decoded.subject shouldBe userId.toString()

            // Verify the refresh token was actually persisted and is valid
            val verified =
                refreshTokenRepository.verifyAndInvalidateRefreshToken(
                    result.accessToken.refreshToken
                )
            verified.shouldNotBeNull()
            verified.userId shouldBe userId
        }

        test("returns error response for missing user") {
            val result = userRepository.login(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
            result shouldBe Login.Response.InvalidCredentials
        }

        test("returns error response for wrong password") {
            userRepository
                .createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
                .shouldNotBeNull()
            val result = userRepository.login(TestConstants.USERNAME, "wrong password")
            result shouldBe Login.Response.InvalidCredentials
        }
    }

    testSuite("getUsername") {
        test("returns username for valid id") {
            val userId =
                userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
            userId.shouldNotBeNull()
            userRepository.getUsername(userId) shouldBe TestConstants.USERNAME
        }

        test("returns null for invalid id") {
            userRepository.getUsername(Uuid.random()) shouldBe null
        }
    }
}
