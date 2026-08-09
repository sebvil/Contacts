package com.sebastianvm.contacts.networking

import com.sebastianvm.contacts.di.createTestAppGraph
import com.sebastianvm.contacts.dto.ContactsResponse
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import kotlin.uuid.Uuid

val KtorContactsApiServiceTest by testSuite {
    test("GET contacts fetches contacts") {
        val mockEngine = MockEngine.Queue()
        val graph = createTestAppGraph {
            engine = mockEngine
        }

        val contact1 = ContactsResponse(id = Uuid.random(), "Elliot")
        val contact2 = ContactsResponse(id = Uuid.random(), "Darlene")
        mockEngine.enqueueHandlerForPath(
            path = "/contacts",
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

        val contactsApiService = KtorContactsApiService(graph.httpClient)
        contactsApiService.fetchContacts() shouldBe listOf(contact1, contact2)
    }
}
