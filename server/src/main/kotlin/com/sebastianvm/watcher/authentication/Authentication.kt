package com.sebastianvm.watcher.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

/**
 * Configures JWT authentication for the application.
 *
 * @param algorithm The [Algorithm] used for token verification.
 */
fun Application.configureAuthentication(algorithm: Algorithm) {
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
    install(Authentication) {
        jwt(JWT_NAME) {
            realm = myRealm

            verifier(JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build())
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

/**
 * Defines an authenticated route using JWT.
 *
 * @param optional If true, authentication is optional for this route.
 * @param build The routing configuration to apply.
 */
fun Route.authenticateRoutes(optional: Boolean = false, build: Route.() -> Unit) {
    authenticate(JWT_NAME, optional = optional, build = build)
}

private const val JWT_NAME = "auth-jwt"
