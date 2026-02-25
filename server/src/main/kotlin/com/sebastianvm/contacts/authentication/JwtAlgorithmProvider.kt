package com.sebastianvm.contacts.authentication

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.ApplicationConfig
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import kotlin.io.encoding.Base64

/**
 * Strategy for providing JWT signing and verification algorithms.
 *
 * Implementations may load keys from configuration, environment variables, or key management
 * services.
 */
interface JwtAlgorithmProvider {
    /**
     * Returns the algorithm used for JWT verification and signing.
     *
     * @return The configured [Algorithm].
     */
    fun getAlgorithm(): Algorithm
}

internal class ECDSA256AlgorithmProvider(private val config: ApplicationConfig) :
    JwtAlgorithmProvider {

    override fun getAlgorithm(): Algorithm {
        val privateKeyString = config.property("jwt.privateKey").getString()
        val publicKeyString = config.property("jwt.publicKey").getString()
        return Algorithm.ECDSA256(loadPublicKey(publicKeyString), loadPrivateKey(privateKeyString))
    }

    private fun loadPrivateKey(pem: String): ECPrivateKey {
        val keyBytes = Base64.Pem.decode(pem)
        val spec = PKCS8EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("EC").generatePrivate(spec) as ECPrivateKey
    }

    private fun loadPublicKey(pem: String): ECPublicKey {
        val keyBytes = Base64.Pem.decode(pem)
        val spec = X509EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("EC").generatePublic(spec) as ECPublicKey
    }
}
