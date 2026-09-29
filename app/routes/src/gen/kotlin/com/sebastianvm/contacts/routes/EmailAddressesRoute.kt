package com.sebastianvm.contacts.routes

import io.ktor.resources.Resource
import kotlin.uuid.Uuid

@Resource("/emailAddresses")
data object EmailAddressesRoute {
    @Resource("{id}")
    data class Id(
        val parent: EmailAddressesRoute = EmailAddressesRoute,
        val id: Uuid,
    )
}
