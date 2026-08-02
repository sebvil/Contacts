package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.applicationTest
import com.sebastianvm.contacts.client
import com.sebastianvm.contacts.contractTest
import com.sebastianvm.contacts.routes.dtos.ContactsRequest
import com.sebastianvm.contacts.routes.dtos.ContactsResponse
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.uuid.Uuid

val ContactRoutesTest by testSuite {

    testSuite("'POST /contacts'") {
        contractTest(executeRequest = {
            post("/contacts") {
                contentType(ContentType.Application.Json)
                setBody(ContactsRequest(Uuid.random(), "Elliot"))
            }
        }, expectedStatus = HttpStatusCode.Created)

        applicationTest("creates and returns contact") {
            val id = Uuid.random()
            val response = client.post(Contacts) {
                contentType(ContentType.Application.Json)
                setBody(ContactsRequest(id, "Elliot"))
            }
            response.status shouldBe HttpStatusCode.Created
            response.body<ContactsResponse>() shouldBe ContactsResponse(id, "Elliot")
        }
    }

}