package com.sebastianvm.contacts.di

import com.sebastianvm.contacts.App
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
@MustUseReturnValues
public interface AppGraph {

    public val app: App
}
