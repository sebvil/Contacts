package com.sebastianvm.contacts.util.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateIn(scope: CoroutineScope, initialValue: T): StateFlow<T> =
    this.stateIn(
        scope = scope,
        started =
            SharingStarted.WhileSubscribed(
                stopTimeoutMillis = DEFAULT_WHILE_SUBSCRIBED_STOP_TIMEOUT_MILLIS
            ),
        initialValue = initialValue,
    )

private const val DEFAULT_WHILE_SUBSCRIBED_STOP_TIMEOUT_MILLIS = 5_000L
