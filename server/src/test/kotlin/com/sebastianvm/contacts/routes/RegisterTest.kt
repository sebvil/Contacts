package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.authentication.AuthenticationTestHelpers.verifyToken
import com.sebastianvm.contacts.db.model.toUser
import com.sebastianvm.contacts.db.schema.Users
import com.sebastianvm.contacts.testutils.TestConstants
import com.sebastianvm.contacts.testutils.baseDbTestSuite
import com.sebastianvm.contacts.testutils.ktorApplicationTest
import com.sebastianvm.contacts.testutils.post
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.call.body
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

val RegisterTest by baseDbTestSuite {
    ktorApplicationTest("register creates user and returns valid tokens", isLoggedIn = false) {
        val response =
            post(
                Register,
                Register.Body(
                    username = TestConstants.USERNAME,
                    password = TestConstants.VALID_PASSWORD,
                ),
            )

        suspendTransaction {
            val user = Users.selectAll().limit(1).map { it.toUser() }.first()
            user.username shouldBe TestConstants.USERNAME
            passwordHasher.verify(TestConstants.VALID_PASSWORD, user.password) shouldBe true
        }
        val body =
            response.body<Register.Response>().shouldBeInstanceOf<Register.Response.Success>()
        verifyToken(body.accessToken.token)
    }

    ktorApplicationTest(
        "register returns error response when user already exists",
        isLoggedIn = false,
    ) {
        userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)
        val response =
            post(
                Register,
                Register.Body(
                    username = TestConstants.USERNAME,
                    password = TestConstants.VALID_PASSWORD,
                ),
            )
        response.body<Register.Response>() shouldBe Register.Response.UserAlreadyExists
    }
}
