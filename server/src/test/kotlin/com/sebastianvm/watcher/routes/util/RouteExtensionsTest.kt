package com.sebastianvm.watcher.routes.util

import com.sebastianvm.watcher.testutils.baseTestSuite
import com.sebastianvm.watcher.testutils.ktorApplicationTest
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder

val RouteExtensionsTest by baseTestSuite {
    ktorApplicationTest("returns 400 for malformed JSON body", isLoggedIn = false) {
        val response =
            rawPost("/login") {
                contentType(ContentType.Application.Json)
                setBody("{ invalid json }")
            }
        response.status shouldBe HttpStatusCode.BadRequest
        response.body<ErrorResponse>().errorMessage shouldBe "Invalid body"
    }

    ktorApplicationTest("returns 415 for non-JSON content type", isLoggedIn = false) {
        val response =
            rawPost("/login") {
                contentType(ContentType.Text.Plain)
                setBody("username=foo&password=bar")
            }
        response.status shouldBe HttpStatusCode.UnsupportedMediaType
        response.body<ErrorResponse>().errorMessage shouldBe "Body must be JSON."
    }
}

context(builder: ApplicationTestBuilder)
private suspend fun rawPost(urlString: String, block: HttpRequestBuilder.() -> Unit): HttpResponse =
    builder.client.request(urlString) {
        method = HttpMethod.Post
        block()
    }
