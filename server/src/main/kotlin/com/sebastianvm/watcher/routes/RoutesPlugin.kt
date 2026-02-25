package com.sebastianvm.watcher.routes

import com.sebastianvm.watcher.authentication.JwtProvider
import com.sebastianvm.watcher.authentication.authenticateRoutes
import com.sebastianvm.watcher.data.RefreshTokenRepository
import com.sebastianvm.watcher.data.UserRepository
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

/**
 * Configures all application routes.
 *
 * Sets up routing for user authentication, registration, and token refresh endpoints.
 *
 * @param userRepository The repository to handle user operations.
 * @param refreshTokenRepository The repository to handle refresh token operations.
 * @param jwtProvider The provider for generating JWT access tokens.
 */
fun Application.configureRoutes(
    userRepository: UserRepository,
    refreshTokenRepository: RefreshTokenRepository,
    jwtProvider: JwtProvider,
) {
    routing {
        login(userRepository = userRepository)
        register(
            userRepository = userRepository,
            refreshTokenRepository = refreshTokenRepository,
            jwtProvider = jwtProvider,
        )
        refresh(refreshTokenRepository = refreshTokenRepository, jwtProvider = jwtProvider)

        authenticateRoutes {
            home(userRepository = userRepository)
            logout(tokenRepository = refreshTokenRepository)
        }
    }
}
