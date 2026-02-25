package com.sebastianvm.watcher.routes.util

import kotlinx.serialization.Serializable

/**
 * Represents an error response with an error message.
 *
 * @property errorMessage The error message describing what went wrong.
 */
@Serializable data class ErrorResponse(val errorMessage: String)
