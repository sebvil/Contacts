package com.sebastianvm.contacts.app.database

import app.cash.sqldelight.db.SqlDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
public interface SqlLiteDriverProvider {

    @Provides private fun provideSqlLiteDriver(): SqlDriver = error("not supported")
}
