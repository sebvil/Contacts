package com.sebastianvm.contacts.di

import com.sebastianvm.contacts.config.DatabaseConfig
import com.sebastianvm.contacts.routes.Routes
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
@MustUseReturnValues
interface AppGraph {

    suspend fun routes(): Routes

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(@Provides databaseConfig: DatabaseConfig): AppGraph
    }
}
