package com.sebastianvm.contacts.db.model

import com.sebastianvm.contacts.db.schema.RefreshTokens
import com.sebastianvm.contacts.model.RefreshToken
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
