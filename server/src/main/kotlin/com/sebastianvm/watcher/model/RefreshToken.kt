package com.sebastianvm.watcher.model

import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Represents a refresh token in the system.
 *
 * @property userId The user ID associated with the refresh token.
 * @property token The refresh token string.
 * @property revokedAt The timestamp when the token was revoked, or null if it is still valid.
 * @property expiresAt The timestamp when the token expires.
 * @property familyId The ID of the refresh token family this token belongs to.
 */
data class RefreshToken(
    val userId: Uuid,
    val token: String,
    val revokedAt: Instant? = null,
    val expiresAt: Instant,
    val familyId: Uuid,
)
