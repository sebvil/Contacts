package com.sebastianvm.watcher.db.model

import com.sebastianvm.watcher.db.schema.Users
import com.sebastianvm.watcher.model.User
import org.jetbrains.exposed.v1.core.ResultRow

/**
 * Extension function to map a [ResultRow] from the [Users] table to a [User] model.
 *
 * @return The mapped [User].
 */
fun ResultRow.toUser() =
    User(
        id = this[Users.id].value,
        username = this[Users.username],
        password = this[Users.password],
    )
