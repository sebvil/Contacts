package com.sebastianvm.contacts.routes

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Resource("/home")
data object Home : ContactsRoute<Home.Response> {

    @Serializable
    sealed interface Response : NetworkResponse {

        /**
         * Represents a successful response for the home endpoint.
         *
         * @property username The username of the authenticated user.
         */
        @Serializable
        data class Success(val username: String) : Response {
            @Transient override val code = HttpStatusCode.OK
        }

        @Serializable
        data object UserNotFound : Response {
            override val code = HttpStatusCode.Unauthorized
        }

        @Serializable
        data object Unauthorized : Response {
            override val code = HttpStatusCode.Unauthorized
        }
    }
}
