package com.sebastianvm.contacts.di

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import java.io.File

@DependencyGraph(AppScope::class)
interface AndroidAppGraph : AppGraph {

    @Provides
    fun produceFileProvider(context: Context): (path: String) -> File {
        return { context.filesDir.resolve(it) }
    }

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): AndroidAppGraph
    }
}
