package com.sebastianvm.contacts

import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication

val ApplicationTest by testSuite {

    test("test root") {
        testApplication {
            application {
                module()
            }
            val response = client.get("/")
            response.status shouldBe HttpStatusCode.OK
            response.bodyAsText() shouldBe "Hello, Ktor!"
        }
    }

}

