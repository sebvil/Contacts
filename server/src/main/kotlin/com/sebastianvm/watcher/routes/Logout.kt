package com.sebastianvm.watcher.routes

import com.sebastianvm.watcher.data.RefreshTokenRepository
import com.sebastianvm.watcher.routes.util.post
import io.ktor.server.routing.Route

fun Route.logout(tokenRepository: RefreshTokenRepository) {
    post<Logout.Response, Logout, Logout.Body> { _, body ->
        tokenRepository.logout(refreshToken = body.refreshToken)
        Logout.Response
    }
}
