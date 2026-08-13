package com.sebastianvm.contacts.app.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import app.cash.sqldelight.driver.worker.expected.Worker
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
public interface SqlLiteDriverProvider {

    @OptIn(ExperimentalWasmJsInterop::class)
    @Provides
    private suspend fun provideSqlLiteDriver(): SqlDriver =
        WebWorkerDriver(
                Worker(
                    scriptURL =
                        js(
                            """new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)"""
                        )
                )
            )
            .also { Database.Schema.create(it).await() }
}
