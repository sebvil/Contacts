package com.sebastianvm.watcher.db.dao

import com.sebastianvm.watcher.db.model.toRefreshToken
import com.sebastianvm.watcher.db.schema.RefreshTokens
import com.sebastianvm.watcher.model.RefreshToken
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.r2dbc.update

/** Data Access Object for refresh tokens. */
interface RefreshTokenDao {
    /**
     * Saves a new refresh token for a user.
     *
     * @param userId The unique identifier of the user.
     * @param refreshToken The hashed refresh token string.
     * @param expiresAt The timestamp when the token expires.
     * @param familyId The ID of the refresh token family this token belongs to.
     */
    suspend fun saveRefreshToken(
        userId: Uuid,
        refreshToken: String,
        expiresAt: Instant,
        familyId: Uuid,
    )

    /**
     * Finds a refresh token and its associated user ID by the token value.
     *
     * @param refreshToken The hashed refresh token string.
     * @return A pair of user ID and [RefreshToken] if found, null otherwise.
     */
    suspend fun findToken(refreshToken: String): RefreshToken?

    /**
     * Invalidates a specific refresh token.
     *
     * @param refreshToken The hashed refresh token string to invalidate.
     */
    suspend fun invalidateRefreshToken(refreshToken: String)

    /**
     * Revokes all refresh tokens for a user belonging to a specific refresh token family.
     *
     * @param familyId The ID of the refresh token family to revoke.
     */
    suspend fun revokeTokenFamily(familyId: Uuid)
}

internal class R2dbcRefreshTokenDao(private val clock: Clock) : RefreshTokenDao {

    override suspend fun saveRefreshToken(
        userId: Uuid,
        refreshToken: String,
        expiresAt: Instant,
        familyId: Uuid,
    ) {
        suspendTransaction {
            RefreshTokens.insert {
                it[RefreshTokens.userId] = userId
                it[RefreshTokens.token] = refreshToken
                it[RefreshTokens.expiresAt] = expiresAt
                it[RefreshTokens.familyId] = familyId
            }
        }
    }

    override suspend fun invalidateRefreshToken(refreshToken: String) {
        suspendTransaction {
            RefreshTokens.update(where = { RefreshTokens.token eq refreshToken }) {
                it[RefreshTokens.revokedAt] = clock.now()
            }
        }
    }

    override suspend fun findToken(refreshToken: String): RefreshToken? {
        return suspendTransaction {
            RefreshTokens.select(
                    RefreshTokens.userId,
                    RefreshTokens.token,
                    RefreshTokens.revokedAt,
                    RefreshTokens.expiresAt,
                    RefreshTokens.familyId,
                )
                .where { RefreshTokens.token eq refreshToken }
                .map { row -> row.toRefreshToken() }
                .firstOrNull()
        }
    }

    override suspend fun revokeTokenFamily(familyId: Uuid) {
        suspendTransaction {
            RefreshTokens.update(
                where = { RefreshTokens.familyId eq familyId and RefreshTokens.revokedAt.isNull() }
            ) {
                it[RefreshTokens.revokedAt] = clock.now()
            }
        }
    }
}
