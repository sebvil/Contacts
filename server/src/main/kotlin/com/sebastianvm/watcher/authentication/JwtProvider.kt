package com.sebastianvm.watcher.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.sebastianvm.watcher.model.AccessToken
import io.ktor.server.config.ApplicationConfig
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaInstant
import kotlin.uuid.Uuid

/**
 * Generates JSON Web Tokens for authenticated users.
 *
 * Example usage:
 * ```
 * val jwtProvider = DefaultJwtProvider(config, algorithm, clock)
 * val accessToken = jwtProvider.getToken(userId, refreshToken)
 * // Returns AccessToken with JWT, expiration time, and refresh token
 * ```
 */
interface JwtProvider {

    /**
     * Generates a JWT access token for the specified user.
     *
     * @param userId The unique identifier of the user for whom to generate the token.
     * @param refreshToken The refresh token to include in the response for token renewal.
     * @return An [AccessToken] containing the JWT, expiration duration, and refresh token.
     */
    fun getToken(userId: Uuid, refreshToken: String): AccessToken
}

internal class DefaultJwtProvider(
    private val config: ApplicationConfig,
    private val algorithm: Algorithm,
    private val clock: Clock,
) : JwtProvider {

    override fun getToken(userId: Uuid, refreshToken: String): AccessToken {
        val issuer = config.property("jwt.issuer").getString()
        val audience = config.property("jwt.audience").getString()
        val token =
            JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withSubject(userId.toString())
                .withIssuedAt(clock.now().toJavaInstant())
                .withExpiresAt(clock.now().plus(JWT_EXPIRATION_TIME).toJavaInstant())
                .sign(algorithm)
        return AccessToken(token, JWT_EXPIRATION_TIME, refreshToken)
    }

    companion object {
        private val JWT_EXPIRATION_TIME = 30.minutes
    }
}
