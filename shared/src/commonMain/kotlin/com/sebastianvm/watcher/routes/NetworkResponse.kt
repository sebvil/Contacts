package com.sebastianvm.watcher.routes

import io.ktor.http.HttpStatusCode

/**
 * Base interface for all API response types.
 *
 * All response objects (success responses and error enums) must implement this interface to ensure
 * type safety in route definitions.
 */
interface NetworkResponse {
    val code: HttpStatusCode
}

interface ErrorNetworkResponse : NetworkResponse
