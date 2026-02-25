package com.sebastianvm.contacts

import android.app.Application
import com.sebastianvm.contacts.di.AndroidAppGraph
import com.sebastianvm.contacts.di.AppGraph
import dev.zacsweers.metro.createGraphFactory

class ContactsApplication : Application() {

    val appGraph: AppGraph by lazy { createGraphFactory<AndroidAppGraph.Factory>().create(this) }
}
