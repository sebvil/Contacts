package com.sebastianvm.contacts.model

import kotlin.time.Duration
import kotlinx.serialization.Serializable

/**
 * Represents an access token with its associated metadata.
 *
 * @property token The JWT access token string.
 * @property expiresIn The duration until the token expires.
 * @property refreshToken The refresh token used to get a new access token.
 */
@Serializable
data class AccessToken(val token: String, val expiresIn: Duration, val refreshToken: String)
