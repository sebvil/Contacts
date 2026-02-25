package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.routes.NetworkResponse
import com.sebastianvm.contacts.routes.PostBody
import com.sebastianvm.contacts.routes.ContactsRoute
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

suspend inline fun <
    reified SR : NetworkResponse,
    reified R : ContactsRoute<SR>,
    reified B : PostBody<R>,
> HttpClient.post(route: R, body: B): SR {
    return post(route) { setBody(body) }.body()
}

suspend inline fun <reified SR : NetworkResponse, reified R : ContactsRoute<SR>> HttpClient.get(
    route: R
): SR {
    return get(route).body()
}
