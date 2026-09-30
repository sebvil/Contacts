package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.contacts.fixtures.makeContact
import com.sebastianvm.contacts.fixtures.toContactsResponse
import com.sebastianvm.core.types.Some
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.HttpMethod
import kotlin.uuid.Uuid

val KtorContactsApiServiceTest by testSuite {
    test("GET contacts fetches contacts") {
        val mockEngine = MockEngine.Queue()
        val client = HttpClientProvider.provideHttpClient(mockEngine)
        val contact1Name = "Elliot"
        val contact2Name = "Darlene"
        val contact1 = ContactResponse(id = Uuid.random(), Some(contact1Name))
        val contact2 = ContactResponse(id = Uuid.random(), Some(contact2Name))
        mockEngine.enqueueHandlerForPath(
            path = "/contacts",
            method = HttpMethod.Get,
            jsonResponse =
                """
                [
                    {
                        "id": "${contact1.id}",
                        "name": "$contact1Name"
                    },
                    {
                        "id": "${contact2.id}",
                        "name": "$contact2Name"
                    }
                ]
                """
                    .trimIndent(),
        )

        val contactsApiService = KtorContactsApiService(client)
        contactsApiService.fetchContacts().getOrThrow() shouldBe listOf(contact1, contact2)
    }

    test("POST contacts creates contact") {
        val mockEngine = MockEngine.Queue()
        val client = HttpClientProvider.provideHttpClient(mockEngine)
        val contact = makeContact()
        mockEngine.enqueueHandlerForPath(
            path = "/contacts",
            method = HttpMethod.Post,
            jsonResponse =
                """
                {
                    "id": "${contact.id}",
                    "name": "${contact.name}"
                }
                """
                    .trimIndent(),
        )

        val contactsApiService = KtorContactsApiService(client)
        contactsApiService.createContact(contact).getOrThrow() shouldBe contact.toContactsResponse()
    }
}
