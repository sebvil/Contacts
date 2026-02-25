package com.sebastianvm.watcher.routes

import com.sebastianvm.watcher.model.AccessToken
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/** Register endpoint for creating new user accounts. */
@Resource("/register")
data object Register : WatcherRoute<Register.Response> {
    /**
     * Request body for the register endpoint.
     *
     * @property username The username for the new account.
     * @property password The password for the new account.
     */
    @Serializable data class Body(val username: String, val password: String) : PostBody<Register>

    /** Response type for the register endpoint. */
    @Serializable
    sealed interface Response : NetworkResponse {
        /**
         * Successful registration response containing an access token.
         *
         * @property accessToken The JWT access token for the newly created user.
         */
        @Serializable
        data class Success(val accessToken: AccessToken) : Response {
            @Transient override val code = HttpStatusCode.OK
        }

        @Serializable
        data object UserAlreadyExists : Response, ErrorNetworkResponse {
            override val code = HttpStatusCode.Conflict
        }
    }
}
