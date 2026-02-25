package com.sebastianvm.watcher.db.schema

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.timestamp

/** Database schema for the refresh_tokens table. */
object RefreshTokens : UuidTable() {
    /** Foreign key reference to the user who owns this refresh token. */
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)

    /** The hashed refresh token value, uniquely indexed. */
    val token = varchar("token", TOKEN_LENGTH).uniqueIndex()

    /** Timestamp when the token was revoked, or null if still active. */
    val revokedAt = timestamp("revoked_at").nullable().default(null)

    /** Timestamp when the token expires. */
    val expiresAt = timestamp("expires_at")

    /** Identifies a group of refresh tokens. */
    val familyId = uuid("family_id").index()

    private const val TOKEN_LENGTH = 255
}
