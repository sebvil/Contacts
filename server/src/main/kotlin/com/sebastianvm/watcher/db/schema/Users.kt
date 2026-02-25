package com.sebastianvm.watcher.db.schema

import org.jetbrains.exposed.v1.core.dao.id.UuidTable

/** Database schema for the users table. */
object Users : UuidTable() {
    /** The unique username for the user. */
    val username = varchar(name = "username", length = 255).uniqueIndex()

    /** The hashed password for the user. */
    val password = varchar(name = "password", length = 255)
}
