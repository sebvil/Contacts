package com.sebastianvm.watcher.model

import kotlin.uuid.Uuid

/**
 * Represents an authenticated user in the system.
 *
 * @property id The user's unique identifier.
 * @property username The user's unique username.
 * @property password The user's hashed password (never stored in plain text).
 */
data class User(val id: Uuid, val username: String, val password: String)
