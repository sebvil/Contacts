package com.sebastianvm.contacts.config

import kotlinx.serialization.Serializable

@Serializable
data class DatabaseConfig(
    val host: String,
    val port: Int,
    val databaseName: String,
    val username: String,
    val password: String,
)
