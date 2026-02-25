package com.sebastianvm.contacts.util

import de.mkammerer.argon2.Argon2Factory

/**
 * Hasher that uses Argon2id for cryptographically secure password hashing. This hasher is used for
 * user passwords which might not have high entropy.
 */
internal class Argon2Hasher : Hasher {
    private val argon2 by lazy { Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id) }

    override fun hash(password: String): String {
        return argon2.hash(ITERATIONS, MEMORY_KIB, PARALLELISM, password.toCharArray())
    }

    override fun verify(password: String, hashedPassword: String): Boolean {
        return argon2.verify(hashedPassword, password.toCharArray())
    }

    companion object {
        // OWASP recommended parameters for Argon2id (moderate security/performance balance)
        // Memory: 19 MB - amount of memory used during hashing
        private const val MEMORY_KIB = 19 * 1024
        // Iterations: 2 - number of passes over the memory
        private const val ITERATIONS = 2
        // Parallelism: 1 - number of parallel threads
        private const val PARALLELISM = 1
    }
}
