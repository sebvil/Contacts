package com.sebastianvm.contacts.database

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.testcontainers.containers.PostgreSQLContainer

@ContributesTo(AppScope::class, replaces = [DatabaseProvider::class])
interface FakeDatabaseProvider {

    @Provides
    @SingleIn(AppScope::class)
    val postgres: PostgreSQLContainer<*>
        get() = PostgreSQLContainer("postgres:16-alpine")

    @Provides
    private suspend fun provideDatabase(providedPostgres: PostgreSQLContainer<*>): R2dbcDatabase {
        val postgres = providedPostgres.apply { start() }
        return R2dbcDatabase.connect(
                url =
                    with(postgres) {
                        "r2dbc:postgresql://$host:${getMappedPort(5432)}/$databaseName"
                    },
                user = postgres.username,
                password = postgres.password,
            )
            .initialize()
    }
}
