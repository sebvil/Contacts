package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.model.AccessToken
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/** Refresh endpoint for getting a new access token. */
@Resource("/refresh")
data object Refresh : ContactsRoute<Refresh.Response> {

    /**
     * Request body for the refresh endpoint.
     *
     * @property refreshToken The refresh token to exchange for a new access token.
     */
    @Serializable data class Body(val refreshToken: String) : PostBody<Refresh>

    /** Response type for the refresh endpoint. */
    @Serializable
    sealed interface Response : NetworkResponse {
        /**
         * Successful refresh response containing a new access token.
         *
         * @property accessToken The new JWT access token.
         */
        @Serializable
        data class Success(val accessToken: AccessToken) : Response {
            @Transient override val code = HttpStatusCode.OK
        }

        @Serializable
        data object InvalidRefreshToken : Response, ErrorNetworkResponse {
            override val code = HttpStatusCode.Unauthorized
        }
    }
}
