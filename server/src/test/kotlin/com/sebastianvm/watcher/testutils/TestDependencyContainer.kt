package com.sebastianvm.watcher.testutils

import com.auth0.jwt.algorithms.Algorithm
import com.sebastianvm.watcher.authentication.DefaultJwtProvider
import com.sebastianvm.watcher.authentication.JwtProvider
import com.sebastianvm.watcher.data.DatabaseRefreshTokenRepository
import com.sebastianvm.watcher.data.DatabaseUserRepository
import com.sebastianvm.watcher.data.RefreshTokenRepository
import com.sebastianvm.watcher.data.UserRepository
import com.sebastianvm.watcher.db.dao.R2dbcRefreshTokenDao
import com.sebastianvm.watcher.db.dao.R2dbcUserDao
import com.sebastianvm.watcher.db.dao.RefreshTokenDao
import com.sebastianvm.watcher.db.schema.Users
import com.sebastianvm.watcher.util.Argon2Hasher
import com.sebastianvm.watcher.util.Hasher
import com.sebastianvm.watcher.util.Sha256Hasher
import io.kotest.assertions.withClue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.MapApplicationConfig
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.firstOrNull
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.mapLazy
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

internal class TestDependencyContainer(val db: R2dbcDatabase? = null) {
    val algorithm: Algorithm by lazy { Algorithm.none() }
    val clock: FakeClock by lazy { FakeClock() }
    val passwordHasher: Hasher by lazy { Argon2Hasher() }
    val tokenHasher: Hasher by lazy { Sha256Hasher() }

    val applicationConfig: ApplicationConfig =
        MapApplicationConfig(
            "jwt.issuer" to "test",
            "jwt.audience" to "test",
            "jwt.realm" to "test",
        )
    val jwtProvider: JwtProvider by lazy {
        DefaultJwtProvider(config = applicationConfig, algorithm = algorithm, clock = clock)
    }

    val refreshTokenDao: RefreshTokenDao by lazy { R2dbcRefreshTokenDao(clock) }

    val refreshTokenRepository: RefreshTokenRepository by lazy {
        DatabaseRefreshTokenRepository(
            refreshTokenDao = refreshTokenDao,
            tokenHasher = tokenHasher,
            clock = clock,
        )
    }
    val userRepository: UserRepository by lazy {
        DatabaseUserRepository(
            userDao = R2dbcUserDao(),
            passwordHasher = passwordHasher,
            refreshTokenRepository = refreshTokenRepository,
            jwtProvider = jwtProvider,
        )
    }

    suspend fun loggedInUserId(): Uuid {
        val id = suspendTransaction {
            Users.select(Users.id)
                .where { Users.username eq TestConstants.USERNAME }
                .mapLazy { it[Users.id].value }
                .firstOrNull()
        }
        return withClue(
            "logged-in user not found, make sure to call `ktorApplicationTest` with `isLoggedIn=true`"
        ) {
            id.shouldNotBeNull()
        }
    }
}
