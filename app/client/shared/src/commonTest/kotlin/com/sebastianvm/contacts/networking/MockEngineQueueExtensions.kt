package com.sebastianvm.contacts.networking

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import org.intellij.lang.annotations.Language

fun MockEngine.Queue.enqueueHandlerForPath(
    path: String,
    method: HttpMethod,
    @Language("JSON") jsonResponse: String,
    status: HttpStatusCode = HttpStatusCode.OK,
) {
    enqueue {
        when (it.url.encodedPath) {
            path if it.method == method -> {
                respond(
                    content = jsonResponse,
                    status = status,
                    headers =
                        headers {
                            append("Content-Type", "application/json")
                        },
                )
            }
            path -> {
                respondError(HttpStatusCode.MethodNotAllowed)
            }
            else -> {
                respondError(HttpStatusCode.NotFound)
            }
        }
    }
}
