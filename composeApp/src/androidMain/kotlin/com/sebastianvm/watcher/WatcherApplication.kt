package com.sebastianvm.watcher

import android.app.Application
import com.sebastianvm.watcher.di.AndroidAppGraph
import com.sebastianvm.watcher.di.AppGraph
import dev.zacsweers.metro.createGraphFactory

class WatcherApplication : Application() {

    val appGraph: AppGraph by lazy { createGraphFactory<AndroidAppGraph.Factory>().create(this) }
}
