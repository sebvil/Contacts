package com.sebastianvm.contacts.util

/**
 * Interface for securely hashing and verifying passwords.
 *
 * Implementations should use a secure password hashing algorithm (e.g., Argon2, bcrypt, scrypt)
 * that is resistant to brute-force and rainbow table attacks.
 */
interface Hasher {
    /**
     * Hashes a plain-text password using a secure hashing algorithm.
     *
     * @param password The plain-text password to hash
     * @return The hashed password string, suitable for storage in a database
     */
    fun hash(password: String): String

    /**
     * Verifies that a plain-text password matches a previously hashed password.
     *
     * @param password The plain-text password to verify
     * @param hashedPassword The previously hashed password to compare against
     * @return `true` if the password matches, `false` otherwise
     */
    fun verify(password: String, hashedPassword: String): Boolean
}
