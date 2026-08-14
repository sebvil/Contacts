package com.sebastianvm.contacts.core.corutines

import kotlinx.coroutines.CancellationException

@Suppress("TooGenericExceptionCaught")
suspend inline fun <R> runCatchingSuspend(block: suspend () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
