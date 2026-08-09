package com.sebastianvm.contacts.di

import com.sebastianvm.contacts.App
import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.features.contacts.list.ContactListPresenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.createDynamicGraph

fun createTestAppGraph(bindings: FakeBindings.Builder.() -> Unit = {}) =
    createDynamicGraph<TestAppGraph>(FakeBindings.Builder().apply { bindings() }.build())

@DependencyGraph(AppScope::class)
interface TestAppGraph {

    val contactListPresenter: ContactListPresenter
    val app: App
    val contactsRepository: ContactsRepository
}
