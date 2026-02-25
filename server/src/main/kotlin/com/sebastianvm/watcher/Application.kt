package com.sebastianvm.watcher

import com.sebastianvm.watcher.authentication.DefaultJwtProvider
import com.sebastianvm.watcher.authentication.ECDSA256AlgorithmProvider
import com.sebastianvm.watcher.authentication.configureAuthentication
import com.sebastianvm.watcher.data.DatabaseRefreshTokenRepository
import com.sebastianvm.watcher.data.DatabaseUserRepository
import com.sebastianvm.watcher.db.configureDatabase
import com.sebastianvm.watcher.db.dao.R2dbcRefreshTokenDao
import com.sebastianvm.watcher.db.dao.R2dbcUserDao
import com.sebastianvm.watcher.routes.configureRoutes
import com.sebastianvm.watcher.util.Argon2Hasher
import com.sebastianvm.watcher.util.Sha256Hasher
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import kotlin.time.Clock

/** Main entry point for the application. */
fun main() {
    embeddedServer(
            Netty,
            environment = applicationEnvironment { config = ApplicationConfig("application.conf") },
            configure = {
                connector {
                    port = SERVER_PORT
                    host = "0.0.0.0"
                }
            },
            module = Application::module,
        )
        .start(wait = true)
}

private suspend fun Application.module() {
    configureExternalPlugins()
    configureDatabase()

    val algorithm = ECDSA256AlgorithmProvider(environment.config).getAlgorithm()

    val clock = Clock.System
    val jwtProvider =
        DefaultJwtProvider(config = environment.config, algorithm = algorithm, clock = clock)
    val passwordHasher = Argon2Hasher()
    val refreshTokenRepository =
        DatabaseRefreshTokenRepository(
            refreshTokenDao = R2dbcRefreshTokenDao(clock = clock),
            tokenHasher = Sha256Hasher(),
            clock = clock,
        )
    val userRepository =
        DatabaseUserRepository(
            userDao = R2dbcUserDao(),
            passwordHasher = passwordHasher,
            jwtProvider = jwtProvider,
            refreshTokenRepository = refreshTokenRepository,
        )

    configureAuthentication(algorithm)
    configureRoutes(
        userRepository = userRepository,
        refreshTokenRepository = refreshTokenRepository,
        jwtProvider = jwtProvider,
    )
}

/**
 * Configures third-party Ktor plugins required by the application.
 *
 * This includes:
 * - Resources plugin for type-safe routing
 * - ContentNegotiation plugin with JSON serialization
 */
fun Application.configureExternalPlugins() {
    install(Resources)
    install(ContentNegotiation) { json() }
}
