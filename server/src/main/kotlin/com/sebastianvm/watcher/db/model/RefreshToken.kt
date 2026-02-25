package com.sebastianvm.watcher.db.model

import com.sebastianvm.watcher.db.schema.RefreshTokens
import com.sebastianvm.watcher.model.RefreshToken
import org.jetbrains.exposed.v1.core.ResultRow

/**
 * Extension function to map a [ResultRow] from the [RefreshTokens] table to a [RefreshToken] model.
 *
 * @return The mapped [RefreshToken].
 */
fun ResultRow.toRefreshToken() =
    RefreshToken(
        userId = this[RefreshTokens.userId].value,
        token = this[RefreshTokens.token],
        revokedAt = this[RefreshTokens.revokedAt],
        expiresAt = this[RefreshTokens.expiresAt],
        familyId = this[RefreshTokens.familyId],
    )
