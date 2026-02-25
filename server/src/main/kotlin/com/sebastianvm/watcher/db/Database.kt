package com.sebastianvm.watcher.db

import com.sebastianvm.watcher.db.schema.RefreshTokens
import com.sebastianvm.watcher.db.schema.Users
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

/**
 * Configures the database for the application.
 *
 * Initializes an H2 database at the default path and creates the necessary schema.
 */
suspend fun Application.configureDatabase() {
    createDatabase(path = "r2dbc:h2:file:///./watcher", developmentMode = developmentMode)
}

/**
 * Creates and initializes the database with the required schema.
 *
 * @param path The R2DBC connection string for the database.
 * @param developmentMode If true, drops existing tables before creating them.
 * @return The initialized R2dbcDatabase instance.
 */
suspend fun createDatabase(path: String, developmentMode: Boolean): R2dbcDatabase {
    val db = R2dbcDatabase.connect(path)

    suspendTransaction {
        if (developmentMode) {
            SchemaUtils.drop(Users, RefreshTokens)
        }
        SchemaUtils.create(Users, RefreshTokens)
    }
    return db
}
