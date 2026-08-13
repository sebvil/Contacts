package com.sebastianvm.contacts.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

/**
 * Reusable glue for the "offline-first" pattern: reactively observe locally-cached data while a
 * network fetch persists its result back into that same local store.
 *
 * Every new collector of [observe] kicks off a background [refresh] (mirroring [fetchRemote]'s
 * result into the local store via [persist]) and then streams whatever [readLocal] emits, so a
 * successful refresh becomes visible without any extra wiring. Call [refresh] directly (e.g. from a
 * pull-to-refresh or retry action) to trigger a sync without resubscribing to [observe].
 *
 * A repository holds one instance per query it needs to serve this way (e.g. "all contacts" and, in
 * the future, "a single contact by id" would each get their own instance).
 */
class OfflineFirstSource<Local, Remote>(
    private val readLocal: () -> Flow<Local>,
    private val fetchRemote: suspend () -> Result<Remote>,
    private val persist: suspend (Remote) -> Unit,
) {

    fun observe(): Flow<Local> = channelFlow {
        launch { refresh() }
        readLocal().collect { send(it) }
    }

    suspend fun refresh() {
        fetchRemote().getOrNull()?.let { persist(it) }
    }
}
