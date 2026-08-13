package com.sebastianvm.contacts.core.corutines

import kotlinx.coroutines.CancellationException

@Suppress("TooGenericExceptionCaught")
suspend inline fun <T, R> T.runCatchingSuspend(block: suspend T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
