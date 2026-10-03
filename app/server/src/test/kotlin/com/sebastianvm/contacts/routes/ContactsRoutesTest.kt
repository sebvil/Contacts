package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.contacts.fixtures.ContactFixture
import com.sebastianvm.contacts.fixtures.createContacts
import com.sebastianvm.contacts.testUtils.applicationTest
import com.sebastianvm.contacts.testUtils.bodyShouldBe
import com.sebastianvm.contacts.testUtils.json
import com.sebastianvm.contacts.testUtils.ktorTestSuite
import com.sebastianvm.contacts.testUtils.post
import com.sebastianvm.contacts.testUtils.to
import com.sebastianvm.core.types.Some
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject

val ContactRoutesTest by ktorTestSuite {
    testSuite("'POST /contacts'") {
        applicationTest("creates and returns contact") {
            val id = Uuid.random()
            val response =
                client.post(
                    urlString = "/contacts",
                    body =
                        json(
                            "id" to id,
                            "name" to "Elliot",
                            "birthday" to "1986-09-17",
                        ),
                )
            response.status shouldBe HttpStatusCode.Created
            response bodyShouldBe
                json(
                    "id" to id,
                    "name" to "Elliot",
                    "birthday" to "1986-09-17",
                )
            appGraph.contactsRepository().getContactById(id) shouldBe
                ContactResponse(
                    id = id,
                    name = Some("Elliot"),
                    birthday =
                        Some(
                            LocalDate(
                                year = 1986,
                                month = 9,
                                day = 17,
                            )
                        ),
                )
        }
    }

    testSuite("'GET /contacts'") {
        applicationTest("returns all contacts") {
            val contactId0 = Uuid.random()
            val contactId1 = Uuid.random()
            val contacts =
                listOf(
                    ContactFixture(
                        id = contactId0,
                        name = "Elliot",
                        birthday =
                            LocalDate(
                                year = 1986,
                                month = 9,
                                day = 17,
                            ),
                    ),
                    ContactFixture(id = contactId1, name = "Darlene"),
                )
            createContacts(contacts)

            val response = client.get(urlString = "/contacts")
            response.status shouldBe HttpStatusCode.OK
            response.body<List<JsonObject>>() shouldBe
                listOf(
                    json("id" to contactId0, "name" to "Elliot", "birthday" to "1986-09-17"),
                    json("id" to contactId1, "name" to "Darlene", "birthday" to JsonNull),
                )
        }
    }
}
