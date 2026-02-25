package com.sebastianvm.watcher.data

import com.sebastianvm.watcher.db.dao.RefreshTokenDao
import com.sebastianvm.watcher.model.RefreshTokenWithUserId
import com.sebastianvm.watcher.util.Hasher
import java.security.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.uuid.Uuid
import org.jetbrains.annotations.VisibleForTesting

/**
 * Repository for managing refresh tokens. Handles generation and verification of refresh tokens for
 * users.
 */
interface RefreshTokenRepository {
    /**
     * Generates a new refresh token for the specified user and saves it to the storage.
     *
     * @param userId The unique identifier of the user.
     * @return The generated refresh token.
     */
    suspend fun generateAndSaveRefreshToken(userId: Uuid): String

    /**
     * Verifies the provided refresh token and invalidates it if valid. If the token is already
     * revoked, all tokens for the user, belonging to the same refresh token family, will be
     * revoked.
     *
     * @param refreshToken The refresh token to verify.
     * @return The refresh token if valid, null otherwise.
     */
    suspend fun verifyAndInvalidateRefreshToken(refreshToken: String): RefreshTokenWithUserId?

    suspend fun logout(refreshToken: String)
}

internal class DatabaseRefreshTokenRepository(
    private val refreshTokenDao: RefreshTokenDao,
    private val tokenHasher: Hasher,
    private val clock: Clock,
) : RefreshTokenRepository {

    override suspend fun generateAndSaveRefreshToken(userId: Uuid): String {
        return generateAndSaveRefreshToken(userId = userId, familyId = Uuid.random())
    }

    override suspend fun verifyAndInvalidateRefreshToken(
        refreshToken: String
    ): RefreshTokenWithUserId? {
        val hashedToken = tokenHasher.hash(refreshToken)
        val token = refreshTokenDao.findToken(hashedToken)

        if (token != null && token.revokedAt == null && clock.now() < token.expiresAt) {
            val newRefreshToken =
                generateAndSaveRefreshToken(userId = token.userId, familyId = token.familyId)
            refreshTokenDao.invalidateRefreshToken(hashedToken)
            return RefreshTokenWithUserId(token.userId, newRefreshToken)
        } else if (token != null && token.revokedAt != null) {
            refreshTokenDao.revokeTokenFamily(familyId = token.familyId)
        }
        return null
    }

    override suspend fun logout(refreshToken: String) {
        val hashedToken = tokenHasher.hash(refreshToken)
        val token = refreshTokenDao.findToken(hashedToken) ?: return
        refreshTokenDao.revokeTokenFamily(familyId = token.familyId)
    }

    private suspend fun generateAndSaveRefreshToken(userId: Uuid, familyId: Uuid): String {
        val token = generateRefreshToken()
        val expiresAt = clock.now() + EXPIRATION_TIME
        val hashedToken = tokenHasher.hash(token)
        refreshTokenDao.saveRefreshToken(
            userId = userId,
            refreshToken = hashedToken,
            expiresAt = expiresAt,
            familyId = familyId,
        )
        return token
    }

    private fun generateRefreshToken(): String {
        val bytes = ByteArray(TOKEN_LENGTH)
        SecureRandom().nextBytes(bytes)
        return Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(bytes)
    }

    companion object {

        @VisibleForTesting val EXPIRATION_TIME = 30.days
        private const val TOKEN_LENGTH = 32 // 256 bits
    }
}
