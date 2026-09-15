package com.sebastianvm.contacts.app.database

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlin.uuid.Uuid

@ContributesTo(AppScope::class)
@BindingContainer
public object DatabaseProvider {

    @Provides
    @SingleIn(AppScope::class)
    public fun provideDatabase(driver: SqlDriver): Database =
        Database(
            driver = driver,
            contactAdapter =
                Contact.Adapter(
                    object : ColumnAdapter<Uuid, String> {
                        override fun decode(databaseValue: String): Uuid {
                            return Uuid.parse(databaseValue)
                        }

                        override fun encode(value: Uuid): String {
                            return value.toString()
                        }
                    }
                ),
        )

    @Provides
    public fun provideContactQueries(database: Database): ContactsQueries = database.contactsQueries
}
