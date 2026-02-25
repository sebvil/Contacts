package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.authentication.JwtProvider
import com.sebastianvm.contacts.db.dao.UserDao
import com.sebastianvm.contacts.routes.Login
import com.sebastianvm.contacts.util.Hasher
import kotlin.uuid.Uuid

/**
 * Repository for user-related data operations.
 *
 * This interface provides the data layer abstraction for user management, handling business logic
 * such as password hashing before delegating to the DAO layer.
 */
interface UserRepository {
    /**
     * Creates a new user with the given credentials.
     *
     * The password will be securely hashed before storage.
     *
     * @param username The username for the new user.
     * @param password The plain-text password for the new user.
     * @return The generated user ID, or null if the user already exists.
     */
    suspend fun createUser(username: String, password: String): Uuid?

    /**
     * Authenticates a user with the given credentials and generates an access token.
     *
     * @param username The username to authenticate.
     * @param password The plain-text password to verify.
     * @return A Login.Response containing either the authentication token or an error.
     */
    suspend fun login(username: String, password: String): Login.Response

    suspend fun getUsername(userId: Uuid): String?
}

internal class DatabaseUserRepository(
    private val userDao: UserDao,
    private val passwordHasher: Hasher,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider,
) : UserRepository {
    override suspend fun createUser(username: String, password: String): Uuid? {
        val hashedPassword = passwordHasher.hash(password)
        return userDao.createUser(username, hashedPassword)
    }

    override suspend fun login(username: String, password: String): Login.Response {
        val user = userDao.getUser(username)
        return when {
            user == null -> Login.Response.InvalidCredentials
            !passwordHasher.verify(password = password, hashedPassword = user.password) ->
                Login.Response.InvalidCredentials

            else -> {
                val refreshToken = refreshTokenRepository.generateAndSaveRefreshToken(user.id)
                Login.Response.Success(
                    accessToken =
                        jwtProvider.getToken(userId = user.id, refreshToken = refreshToken)
                )
            }
        }
    }

    override suspend fun getUsername(userId: Uuid): String? {
        return userDao.getUsername(userId)
    }
}
