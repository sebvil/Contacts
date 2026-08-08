package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.domain.Contact
import com.sebastianvm.contacts.dto.ContactsResponse
import com.sebastianvm.contacts.networking.FakeContactsApiService
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first

val OnlineOnlyContactsRepositoryTest by testSuite {
    test("getContacts maps DTOs to domain contacts") {
        val contact1 = ContactsResponse(id = Uuid.random(), name = "Elliot")
        val contact2 = ContactsResponse(id = Uuid.random(), name = "Darlene")
        val contactsApiService = FakeContactsApiService(listOf(contact1, contact2))

        val repository = OnlineOnlyContactsRepository(contactsApiService)

        repository.getContacts().first() shouldBe
            listOf(Contact(contact1.id, contact1.name), Contact(contact2.id, contact2.name))
    }
}
