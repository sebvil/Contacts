package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.core.corutines.runCatchingSuspend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

suspend inline fun <reified T : Any, reified B : Any, reified R : Any> HttpClient.post(
    resource: T,
    body: B,
): Result<R> =
    runCatchingSuspend {
            post(resource) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
                .body<R>()
        }
        .onFailure { println(it) }

suspend inline fun <reified T : Any, reified R : Any> HttpClient.get(resource: T): Result<R> =
    runCatchingSuspend {
            get(resource).body<R>()
        }
        .onFailure { println(it) }
