package com.sebastianvm.watcher.util

import java.security.MessageDigest
import org.h2.security.SHA256

/**
 * SHA-256 hasher for cryptographically random tokens.
 *
 * Note: This should only be used for high-entropy random tokens (like refresh tokens), NOT for user
 * passwords. Use Argon2Hasher for passwords.
 */
internal class Sha256Hasher : Hasher {

    override fun hash(password: String): String {
        return String(SHA256.getHash(/* data= */ password.toByteArray(), /* nullData= */ false))
    }

    override fun verify(password: String, hashedPassword: String): Boolean {
        val computedHash = hash(password)
        // Use constant-time comparison to prevent timing attacks
        return MessageDigest.isEqual(computedHash.toByteArray(), hashedPassword.toByteArray())
    }
}
