package com.sebastianvm.watcher.routes

/**
 * Base interface for all Watcher API routes.
 *
 * @param R The type of server response this route returns.
 */
interface WatcherRoute<R : NetworkResponse>
