package com.sebastianvm.contacts.features.contacts.list

import com.sebastianvm.contacts.data.FakeContactsRepository
import com.sebastianvm.contacts.designsys.components.ContactListItem
import com.sebastianvm.contacts.fixtures.makeContacts
import com.sebastianvm.contacts.testutils.awaitState
import com.slack.circuit.test.test
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

val ContactListPresenterTest by testSuite {
    test("emits loading then Data when repository returns no contacts") {
        val subject = ContactListPresenter(FakeContactsRepository())
        subject.test {
            awaitState() shouldBe ContactListState.Loading
            awaitState() shouldBe ContactListState.Data(emptyList())
        }
    }

    test("emits loading then Data with mapped contacts when repository returns contacts") {
        val (contact1, contact2) = makeContacts()
        val repository = FakeContactsRepository(listOf(contact1, contact2))

        val subject = ContactListPresenter(repository)
        subject.test {
            awaitState() shouldBe ContactListState.Loading
            awaitState() shouldBe
                ContactListState.Data(
                    listOf(
                        ContactListItem.State(id = contact1.id, name = contact1.name),
                        ContactListItem.State(id = contact2.id, name = contact2.name),
                    )
                )
        }
    }

    test("emits loading then Error when repository throws") {
        val repository =
            FakeContactsRepository().apply { getContactsError = IllegalStateException("boom") }

        val subject = ContactListPresenter(repository)
        subject.test {
            awaitState() shouldBe ContactListState.Loading
            awaitState() shouldBe ContactListState.Error
        }
    }
}
