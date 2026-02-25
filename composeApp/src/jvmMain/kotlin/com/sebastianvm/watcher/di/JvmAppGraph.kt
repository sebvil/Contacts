package com.sebastianvm.watcher.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import java.io.File

@DependencyGraph(AppScope::class)
interface JvmAppGraph : AppGraph {

    @Provides
    fun provideFileProvider(): (fileName: String) -> File = { fileName ->
        File(System.getProperty("java.io.tmpdir"), fileName)
    }
}
