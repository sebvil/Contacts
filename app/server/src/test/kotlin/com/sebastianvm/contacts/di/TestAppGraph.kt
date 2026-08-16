package com.sebastianvm.contacts.di

import com.sebastianvm.contacts.repository.ContactsRepository
import com.sebastianvm.contacts.routes.Routes
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

@DependencyGraph(AppScope::class)
interface TestAppGraph {

    suspend fun routes(): Routes

    suspend fun contactsRepository(): ContactsRepository

    suspend fun database(): R2dbcDatabase
}
