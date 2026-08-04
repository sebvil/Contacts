package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.dto.toContactsResponse
import com.sebastianvm.contacts.fixtures.makeContact
import com.sebastianvm.contacts.fixtures.makeContacts
import com.sebastianvm.contacts.fixtures.toContactsRequest
import com.sebastianvm.contacts.testUtils.applicationTest
import com.sebastianvm.contacts.testUtils.contractTest
import com.sebastianvm.contacts.testUtils.ktorTestSuite
import com.sebastianvm.contacts.testUtils.post
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

val ContactRoutesTest by ktorTestSuite {
    testSuite("'POST /contacts'") {
        contractTest(
            executeRequest = {
                post(urlString = "/contacts", body = makeContact().toContactsRequest())
            },
            expectedStatus = HttpStatusCode.Created,
        )

        applicationTest("creates and returns contact") {
            val contact = makeContact()
            val response = client.post(Contacts, contact.toContactsRequest())
            response.status shouldBe HttpStatusCode.Created
            response.body<ContactsResponse>() shouldBe contact.toContactsResponse()
            appGraph.contactsRepository().getContactById(contact.id) shouldBe contact
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
            val contacts = makeContacts()
            val contactsRepository = appGraph.contactsRepository()
            contacts.forEach {
                @Suppress("RETURN_VALUE_NOT_USED_COERCION") contactsRepository.createContact(it)
            }
            val response = client.get(resource = Contacts)
            response.status shouldBe HttpStatusCode.OK
            response.body<List<ContactsResponse>>() shouldBe
                contacts.map { it.toContactsResponse() }
        }
    }
}
