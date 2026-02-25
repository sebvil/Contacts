package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.testutils.TestConstants
import com.sebastianvm.contacts.testutils.baseDbTestSuite
import com.sebastianvm.contacts.testutils.get
import com.sebastianvm.contacts.testutils.ktorApplicationTest
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

val HomeTest by baseDbTestSuite {
    ktorApplicationTest("Home returns current user username", isLoggedIn = true) {
        val response = get(Home)
        response.status shouldBe HttpStatusCode.OK
        val body = response.body<Home.Response>() as Home.Response.Success
        body.username shouldBe TestConstants.USERNAME
    }
}
