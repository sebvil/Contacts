package com.sebastianvm.contacts.database

import com.sebastianvm.contacts.config.DatabaseConfig
import com.sebastianvm.contacts.database.tables.ContactsTable
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

@ContributesTo(AppScope::class)
interface DatabaseProvider {

    @Provides
    @SingleIn(AppScope::class)
    private suspend fun provideDatabase(databaseConfig: DatabaseConfig): R2dbcDatabase {
        return with(databaseConfig) {
            R2dbcDatabase.connect(
                    url = "r2dbc:postgresql://$host:$port/$databaseName",
                    driver = "postgresql",
                    user = username,
                    password = password,
                )
                .initialize()
        }
    }
}

suspend fun R2dbcDatabase.initialize(): R2dbcDatabase {
    suspendTransaction(db = this) { SchemaUtils.create(ContactsTable) }
    return this
}
