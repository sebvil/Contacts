package com.sebastianvm.contacts.data

import com.sebastianvm.contacts.fixtures.makeContacts
import com.sebastianvm.contacts.fixtures.toContactsResponse
import com.sebastianvm.contacts.networking.FakeContactsApiService
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

val OfflineFirstContactRepositoryTest by testSuite {
    test("getContacts reads from the local data source, not the network") {
        val (contact1, contact2) = makeContacts()
        val localContactsDataSource = FakeLocalContactsDataSource(listOf(contact1, contact2))
        val repository =
            OfflineFirstContactRepository(localContactsDataSource, FakeContactsApiService())

        repository.getContacts().first() shouldBe listOf(contact1, contact2)
    }

    test("refreshContacts maps DTOs to domain contacts and persists them locally") {
        val (contact1, contact2) = makeContacts()
        val contactsApiService =
            FakeContactsApiService(
                listOf(contact1.toContactsResponse(), contact2.toContactsResponse())
            )
        val localContactsDataSource = FakeLocalContactsDataSource()
        val repository = OfflineFirstContactRepository(localContactsDataSource, contactsApiService)

        repository.refreshContacts()
        repository.getContacts().first() shouldBe listOf(contact1, contact2)
    }

    test("refresh leaves local data untouched when the network call fails") {
        val (contact1) = makeContacts()
        val error = IllegalStateException("boom")
        val contactsApiService = FakeContactsApiService().apply { fetchContactsError = error }
        val localContactsDataSource = FakeLocalContactsDataSource(listOf(contact1))
        val repository = OfflineFirstContactRepository(localContactsDataSource, contactsApiService)

        repository.refreshContacts()
        repository.getContacts().first() shouldBe listOf(contact1)
    }
}
