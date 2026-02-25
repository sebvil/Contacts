package com.sebastianvm.contacts.routes

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/logout")
data object Logout : ContactsRoute<Logout.Response> {

    @Serializable data class Body(val refreshToken: String) : PostBody<Logout>

    @Serializable
    data object Response : NetworkResponse {
        override val code = HttpStatusCode.OK
    }
}
