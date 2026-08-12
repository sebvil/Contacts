package com.sebastianvm.contacts.di

import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.data.FakeContactsRepository
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine

@BindingContainer
class FakeBindings(
    private val engine: HttpClientEngine = MockEngine.Queue(),
    private val contactsRepository: ContactsRepository = FakeContactsRepository(),
) {

    @Provides fun provideHttpClientEngine(): HttpClientEngine = engine

    @Provides fun provideContactsRepository(): ContactsRepository = contactsRepository

    class Builder {
        var engine: HttpClientEngine = MockEngine.Queue()
        var contactsRepository: FakeContactsRepository = FakeContactsRepository()

        fun build(): FakeBindings =
            FakeBindings(engine = engine, contactsRepository = contactsRepository)
    }
}
