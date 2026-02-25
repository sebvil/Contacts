package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.model.AccessToken
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/** Login endpoint for user authentication. */
@Resource("/login")
data object Login : ContactsRoute<Login.Response> {
    /**
     * Request body for the login endpoint.
     *
     * @property username The username to authenticate.
     * @property password The password to authenticate.
     */
    @Serializable data class Body(val username: String, val password: String) : PostBody<Login>

    /** Response type for the login endpoint. */
    @Serializable
    sealed interface Response : NetworkResponse {

        /**
         * Successful login response containing an access token.
         *
         * @property accessToken The JWT access token for the authenticated user.
         */
        @Serializable
        data class Success(val accessToken: AccessToken) : Response {
            @Transient override val code = HttpStatusCode.OK
        }

        @Serializable
        data object InvalidCredentials : Response, ErrorNetworkResponse {
            override val code = HttpStatusCode.Unauthorized
        }
    }
}
