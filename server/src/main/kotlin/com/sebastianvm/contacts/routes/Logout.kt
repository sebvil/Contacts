package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.data.RefreshTokenRepository
import com.sebastianvm.contacts.routes.util.post
import io.ktor.server.routing.Route

fun Route.logout(tokenRepository: RefreshTokenRepository) {
    post<Logout.Response, Logout, Logout.Body> { _, body ->
        tokenRepository.logout(refreshToken = body.refreshToken)
        Logout.Response
    }
}
