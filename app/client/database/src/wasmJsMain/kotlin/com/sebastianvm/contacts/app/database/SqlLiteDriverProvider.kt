package com.sebastianvm.contacts.app.database

import app.cash.sqldelight.db.SqlDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
@BindingContainer
public object SqlLiteDriverProvider {

    @Provides private fun provideSqlLiteDriver(): SqlDriver = error("not supported")
}
