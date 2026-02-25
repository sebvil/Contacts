package com.sebastianvm.watcher.routes

import com.sebastianvm.watcher.authentication.JwtProvider
import com.sebastianvm.watcher.data.RefreshTokenRepository
import com.sebastianvm.watcher.routes.util.post
import io.ktor.server.routing.Route

/**
 * Configures the refresh token route for renewing access tokens.
 *
 * @param refreshTokenRepository The repository to handle refresh token operations.
 * @param jwtProvider The provider for generating new JWT access tokens.
 */
fun Route.refresh(refreshTokenRepository: RefreshTokenRepository, jwtProvider: JwtProvider) {
    post<Refresh.Response, Refresh, Refresh.Body> { _, body ->
        val refreshTokenWithUserId =
            refreshTokenRepository.verifyAndInvalidateRefreshToken(body.refreshToken)
        if (refreshTokenWithUserId != null) {
            val accessToken =
                jwtProvider.getToken(
                    userId = refreshTokenWithUserId.userId,
                    refreshToken = refreshTokenWithUserId.token,
                )
            Refresh.Response.Success(accessToken = accessToken)
        } else {
            Refresh.Response.InvalidRefreshToken
        }
    }
}
