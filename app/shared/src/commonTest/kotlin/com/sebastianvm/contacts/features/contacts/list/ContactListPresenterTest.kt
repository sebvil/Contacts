package com.sebastianvm.contacts.features.contacts.list

import com.sebastianvm.contacts.data.FakeContactsRepository
import com.sebastianvm.contacts.designsys.components.ContactListItem
import com.sebastianvm.contacts.fixtures.makeContacts
import com.sebastianvm.contacts.testutils.awaitState
import com.slack.circuit.test.test
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy

// Loading is only shown once a request has been in flight for longer than 200ms, and once shown
// it stays up for at least another 200ms, so anything that resolves comfortably inside/outside
// those windows exercises a specific branch of that logic.
private val FAST_RESPONSE = 50.milliseconds
private val MEDIUM_RESPONSE = 300.milliseconds
private val SLOW_RESPONSE = 500.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
val ContactListPresenterTest by testSuite {
    test("emits Idle then Data when repository returns no contacts quickly") {
        val subject = makeSubject()
        subject.test {
            awaitState() shouldBe ContactListState.Idle
            awaitState() shouldBe ContactListState.Data(emptyList())
        }
    }

    test("emits Idle then Data with mapped contacts when repository returns contacts quickly") {
        val (contact1, contact2) = makeContacts()
        val repository =
            FakeContactsRepository(
                initialContacts = listOf(contact1, contact2),
                responseDelay = FAST_RESPONSE,
            )

        val subject = makeSubject(repository)
        subject.test {
            awaitState() shouldBe ContactListState.Idle
            awaitState() shouldBe
                ContactListState.Data(
                    listOf(
                        ContactListItem.State(id = contact1.id, name = contact1.name),
                        ContactListItem.State(id = contact2.id, name = contact2.name),
                    )
                )
        }
    }

    test("emits Idle then Error when repository throws quickly") {
        val repository =
            FakeContactsRepository(responseDelay = FAST_RESPONSE).apply {
                getContactsError = IllegalStateException("boom")
            }

        val subject = makeSubject(repository)
        subject.test {
            awaitState() shouldBe ContactListState.Idle
            awaitState() shouldBe ContactListState.Error
        }
    }

    test("emits Idle, then Loading, then Data when repository is slow to return contacts") {
        // The repository resolves at 500ms, comfortably after the 400ms point where Loading's
        // minimum visible duration ends, so Data should be shown as soon as it arrives with no
        // extra delay.
        val subject = makeSubject(FakeContactsRepository(responseDelay = SLOW_RESPONSE))
        subject.test {
            awaitState() shouldBe ContactListState.Idle
            awaitState() shouldBe ContactListState.Loading

            testScope.advanceTimeBy(299.milliseconds)
            expectNoEvents()
            testScope.advanceTimeBy(1.milliseconds)

            awaitState() shouldBe ContactListState.Data(emptyList())
        }
    }

    test(
        "keeps Loading visible for the minimum duration even when data resolves shortly after it appears"
    ) {
        val subject = makeSubject(FakeContactsRepository(responseDelay = MEDIUM_RESPONSE))
        subject.test {
            awaitState() shouldBe ContactListState.Idle
            awaitState() shouldBe ContactListState.Loading
            testScope.advanceTimeBy(199.milliseconds)
            expectNoEvents()
            testScope.advanceTimeBy(1.milliseconds)

            awaitState() shouldBe ContactListState.Data(emptyList())
        }
    }

    test("emits Idle, then Loading, then Error when repository is slow to throw") {
        // The repository throws at 500ms, comfortably after the 400ms point where Loading's
        // minimum visible duration ends, so Error should be shown as soon as it arrives with no
        // extra delay.
        val repository =
            FakeContactsRepository(responseDelay = SLOW_RESPONSE).apply {
                getContactsError = IllegalStateException("boom")
            }

        val subject = makeSubject(repository)
        subject.test {
            awaitState() shouldBe ContactListState.Idle
            awaitState() shouldBe ContactListState.Loading

            testScope.advanceTimeBy(299.milliseconds)
            expectNoEvents()
            testScope.advanceTimeBy(1.milliseconds)

            awaitState() shouldBe ContactListState.Error
        }
    }

    testSuite("handleEvent") {
        test("TryAgain resets state to Idle, then Loading, then Error") {
            val repository =
                FakeContactsRepository(responseDelay = SLOW_RESPONSE).apply {
                    getContactsError = IllegalStateException("boom")
                }
            val subject = makeSubject(repository)
            subject.test {
                awaitState() shouldBe ContactListState.Idle
                awaitState() shouldBe ContactListState.Loading
                val latestState = awaitItem()

                latestState.state shouldBe ContactListState.Error
                latestState.handleEvent(ContactListUiEvent.TryAgain)
                awaitState() shouldBe ContactListState.Idle
                awaitState() shouldBe ContactListState.Loading
                awaitState() shouldBe ContactListState.Error
            }
        }
    }
}

private fun makeSubject(
    contactsRepository: FakeContactsRepository =
        FakeContactsRepository(responseDelay = FAST_RESPONSE)
) = ContactListPresenter { contactsRepository }
