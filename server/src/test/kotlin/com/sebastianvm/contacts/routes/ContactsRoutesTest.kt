package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.dto.toContactsResponse
import com.sebastianvm.contacts.fixtures.makeContact
import com.sebastianvm.contacts.fixtures.toContactsRequest
import com.sebastianvm.contacts.testUtils.applicationTest
import com.sebastianvm.contacts.testUtils.contractTest
import com.sebastianvm.contacts.testUtils.ktorTestSuite
import com.sebastianvm.contacts.testUtils.post
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
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
}
