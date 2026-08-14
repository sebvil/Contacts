package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.fixtures.makeContact
import com.sebastianvm.contacts.fixtures.toContactsResponse
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.HttpMethod
import kotlin.uuid.Uuid

val KtorContactsApiServiceTest by testSuite {
    test("GET contacts fetches contacts") {
        val mockEngine = MockEngine.Queue()
        val client = HttpClientProvider.provideHttpClient(mockEngine)
        val contact1 = ContactsResponse(id = Uuid.random(), "Elliot")
        val contact2 = ContactsResponse(id = Uuid.random(), "Darlene")
        mockEngine.enqueueHandlerForPath(
            path = "/contacts",
            method = HttpMethod.Get,
            jsonResponse =
                """
                [
                    {
                        "id": "${contact1.id}",
                        "name": "${contact1.name}"
                    },
                    {
                        "id": "${contact2.id}",
                        "name": "${contact2.name}"
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
