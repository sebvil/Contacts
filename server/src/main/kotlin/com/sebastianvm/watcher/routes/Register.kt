package com.sebastianvm.watcher.routes

import com.sebastianvm.watcher.authentication.JwtProvider
import com.sebastianvm.watcher.data.RefreshTokenRepository
import com.sebastianvm.watcher.data.UserRepository
import com.sebastianvm.watcher.routes.util.post
import io.ktor.server.routing.Route

/**
 * Configures the user registration route.
 *
 * @param userRepository The repository to handle user creation.
 * @param refreshTokenRepository The repository to handle refresh token generation.
 * @param jwtProvider The provider for generating JWT access tokens.
 */
fun Route.register(
    userRepository: UserRepository,
    refreshTokenRepository: RefreshTokenRepository,
    jwtProvider: JwtProvider,
) {
    post<Register.Response, Register, Register.Body> { _, body ->
        val userId =
            userRepository.createUser(body.username, body.password)
                ?: return@post Register.Response.UserAlreadyExists
        val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(userId)
        val token = jwtProvider.getToken(userId = userId, refreshToken)
        Register.Response.Success(accessToken = token)
    }
}
