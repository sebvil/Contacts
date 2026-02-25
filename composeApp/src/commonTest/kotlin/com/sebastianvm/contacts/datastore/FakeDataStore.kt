package com.sebastianvm.contacts.datastore

import androidx.datastore.core.DataStore
import com.sebastianvm.contacts.model.AccessToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

class FakeDataStore<T>(initialValue: T) : DataStore<T> {
    private val _data = MutableStateFlow<T>(initialValue)
    override val data: Flow<T>
        get() = _data

    override suspend fun updateData(transform: suspend (t: T) -> T): T {
        return _data.updateAndGet { transform(it) }
    }
}

@ContributesTo(AppScope::class, replaces = [AccessTokenDataStoreProvider::class])
interface FakeAccessTokenDataStoreProvider {
    @Provides
    @SingleIn(AppScope::class)
    fun provideAccessTokenDataStore(): DataStore<AccessToken?> = FakeDataStore(initialValue = null)
}
