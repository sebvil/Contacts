package com.sebastianvm.contacts.app.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
@BindingContainer
public object SqlLiteDriverProvider {

    @Provides
    public suspend fun provideSqlLiteDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:contacts.db").also {
            Database.Schema.create(it).await()
        }
}
