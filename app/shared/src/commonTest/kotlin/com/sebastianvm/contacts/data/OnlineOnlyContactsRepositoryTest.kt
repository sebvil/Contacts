package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.fixtures.makeContacts
import com.sebastianvm.contacts.fixtures.toContactsResponse
import com.sebastianvm.contacts.networking.FakeContactsApiService
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

val OnlineOnlyContactsRepositoryTest by testSuite {
    test("getContacts maps DTOs to domain contacts") {
        val (contact1, contact2) = makeContacts()
        val contactsApiService =
            FakeContactsApiService(
                listOf(contact1.toContactsResponse(), contact2.toContactsResponse())
            )

        val repository = OnlineOnlyContactsRepository(contactsApiService)

        repository.getContacts().first() shouldBe listOf(contact1, contact2)
    }
}
