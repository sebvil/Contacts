package com.sebastianvm.contacts.routes

import io.ktor.resources.Resource
import kotlin.uuid.Uuid

@Resource("/contacts")
data object ContactsRoute {
    @Resource("{id}")
    data class Id(
        val parent: ContactsRoute = ContactsRoute,
        val id: Uuid,
    ) {
        @Resource("/emailAddresses") data class EmailAddresses(val parent: Id)
    }
}
