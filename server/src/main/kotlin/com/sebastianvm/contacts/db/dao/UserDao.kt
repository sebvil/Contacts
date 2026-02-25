package com.sebastianvm.contacts.db.dao

import com.sebastianvm.contacts.db.model.toUser
import com.sebastianvm.contacts.db.schema.Users
import com.sebastianvm.contacts.model.User
import io.r2dbc.spi.R2dbcDataIntegrityViolationException
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.firstOrNull
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.ExposedR2dbcException
import org.jetbrains.exposed.v1.r2dbc.insertAndGetId
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

/**
 * Data Access Object for user database operations.
 *
 * This interface provides low-level database access for user-related operations. Implementations
 * should handle direct database interactions without business logic.
 */
interface UserDao {
    /**
     * Inserts a new user into the database.
     *
     * @param username The username for the new user
     * @param password The hashed password for the new user (should already be hashed)
     * @return The generated user ID, or null if the user already exists.
     */
    suspend fun createUser(username: String, password: String): Uuid?

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The [User] if found, null otherwise.
     */
    suspend fun getUser(username: String): User?

    suspend fun getUsername(userId: Uuid): String?
}

internal class R2dbcUserDao : UserDao {
    override suspend fun createUser(username: String, password: String): Uuid? {
        return try {
            suspendTransaction {
                Users.insertAndGetId {
                        it[Users.username] = username
                        it[Users.password] = password
                    }
                    .value
            }
        } catch (e: ExposedR2dbcException) {
            if (e.cause is R2dbcDataIntegrityViolationException) {
                return null
            } else {
                throw e
            }
        }
    }

    override suspend fun getUser(username: String): User? {
        return suspendTransaction {
            Users.selectAll().where { Users.username eq username }.firstOrNull()?.toUser()
        }
    }

    override suspend fun getUsername(userId: Uuid): String? {
        return suspendTransaction {
            Users.select(Users.username)
                .where { Users.id eq userId }
                .firstOrNull()
                ?.get(Users.username)
        }
    }
}
