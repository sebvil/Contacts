package com.sebastianvm.contacts.app.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
public interface SqlLiteDriverProvider {

    @Provides
    private fun provideSqlLiteDriver(context: Context): SqlDriver =
        AndroidSqliteDriver(Database.Schema.synchronous(), context, name = "contacts.db")
}
