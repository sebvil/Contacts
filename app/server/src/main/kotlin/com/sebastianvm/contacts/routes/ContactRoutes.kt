package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.dto.ContactsRequest
import com.sebastianvm.contacts.dto.toContact
import com.sebastianvm.contacts.dto.toContactsResponse
import com.sebastianvm.contacts.repository.ContactsRepository
import dev.zacsweers.metro.Inject
import io.ktor.http.HttpStatusCode
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing

@Inject
class ContactRoutes(private val contactsRepository: ContactsRepository) {

    context(route: Routing)
    operator fun invoke() {
        with(route) {
            post()
            get()
        }
    }

    private fun Routing.post() {
        post<Contacts, ContactsRequest> { _, contactsRequest ->
            val contact = contactsRepository.createContact(contactsRequest.toContact())
            call.respond(
                status = HttpStatusCode.Created,
                message = contact.toContactsResponse(),
            )
        }
    }

    private fun Routing.get() {
        get<Contacts> { _ ->
            val contactsResponse =
                contactsRepository.getAllContacts().map { it.toContactsResponse() }
            call.respond(
                status = HttpStatusCode.OK,
                message = contactsResponse,
            )
        }
    }
}
