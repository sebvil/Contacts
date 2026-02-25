package com.sebastianvm.watcher.networking

import com.sebastianvm.watcher.routes.NetworkResponse
import com.sebastianvm.watcher.routes.PostBody
import com.sebastianvm.watcher.routes.WatcherRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

suspend inline fun <
    reified SR : NetworkResponse,
    reified R : WatcherRoute<SR>,
    reified B : PostBody<R>,
> HttpClient.post(route: R, body: B): SR {
    return post(route) { setBody(body) }.body()
}

suspend inline fun <reified SR : NetworkResponse, reified R : WatcherRoute<SR>> HttpClient.get(
    route: R
): SR {
    return get(route).body()
}
