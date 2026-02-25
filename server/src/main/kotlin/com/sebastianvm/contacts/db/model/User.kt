package com.sebastianvm.contacts.db.model

import com.sebastianvm.contacts.db.schema.Users
import com.sebastianvm.contacts.model.User
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
