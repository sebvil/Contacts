package com.sebastianvm.contacts.testUtils

import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.JsonObject

suspend inline fun <reified T> HttpClient.post(urlString: String, body: T): HttpResponse {
    return post(urlString = urlString) {
        setBody(body)
    }
}

suspend inline fun <reified T, reified R : Any> HttpClient.post(
    resource: R,
    body: T,
): HttpResponse {
    return post<R>(resource = resource) {
        setBody(body)
    }
}

suspend inline fun HttpClient.post(urlString: String, body: JsonObject): HttpResponse {
    return post(urlString = urlString) {
        setBody(body = body.toString())
    }
}

@IgnorableReturnValue
suspend infix fun HttpResponse.bodyShouldBe(response: JsonObject): JsonObject {
    return this.body<JsonObject>() shouldBe response
}
