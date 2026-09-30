package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.contacts.fixtures.makeContactRequest
import com.sebastianvm.contacts.fixtures.makeContactRequests
import com.sebastianvm.contacts.fixtures.toContactsResponse
import com.sebastianvm.contacts.testUtils.applicationTest
import com.sebastianvm.contacts.testUtils.contractTest
import com.sebastianvm.contacts.testUtils.ktorTestSuite
import com.sebastianvm.contacts.testUtils.post
import com.sebastianvm.core.types.getOrThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

val ContactRoutesTest by ktorTestSuite {
    testSuite("'POST /contacts'") {
        contractTest(
            executeRequest = {
                post(urlString = "/contacts", body = makeContactRequest())
            },
            expectedStatus = HttpStatusCode.Created,
        )

        applicationTest("creates and returns contact") {
            val contact = makeContactRequest()
            val response = client.post(ContactsRoute, contact)
            response.status shouldBe HttpStatusCode.Created
            response.body<ContactResponse>() shouldBe contact.toContactsResponse()
            appGraph.contactsRepository().getContactById(contact.id.getOrThrow()) shouldBe
                contact.toContactsResponse()
        }
    }

    testSuite("'GET /contacts'") {
        contractTest(
            executeRequest = {
                get(urlString = "/contacts")
            },
            expectedStatus = HttpStatusCode.OK,
        )

        applicationTest("returns all contacts") {
            val contacts = makeContactRequests()
            val contactsRepository = appGraph.contactsRepository()
            contacts.forEach {
                @Suppress("RETURN_VALUE_NOT_USED_COERCION") contactsRepository.createContact(it)
            }
            val response = client.get(resource = ContactsRoute)
            response.status shouldBe HttpStatusCode.OK
            response.body<List<ContactResponse>>() shouldBe contacts.map { it.toContactsResponse() }
        }
    }
}
