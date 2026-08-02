package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.routes.dtos.ContactsRequest
import com.sebastianvm.contacts.routes.dtos.ContactsResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.contactRoutes() {
    post<Contacts, ContactsRequest> { _, body ->
        call.respond(
            status = HttpStatusCode.Created,
            message = ContactsResponse(id = body.id, name = body.name),
        )
    }
}
