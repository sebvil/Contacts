package com.sebastianvm.watcher.model

import kotlin.uuid.Uuid

/**
 * Represents a refresh token in the system.
 *
 * @property userId The user ID associated with the refresh token.
 * @property token The refresh token string.
 */
data class RefreshTokenWithUserId(val userId: Uuid, val token: String)
