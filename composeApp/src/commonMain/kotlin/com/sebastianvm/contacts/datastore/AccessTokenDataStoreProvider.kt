package com.sebastianvm.contacts.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import com.sebastianvm.contacts.model.AccessToken
import com.sebastianvm.contacts.util.json.jsonParser
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import java.io.File

@ContributesTo(AppScope::class)
interface AccessTokenDataStoreProvider {

    @Provides
    fun provideSerializer(): Serializer<AccessToken?> =
        KtSerializationSerializer(jsonParser = jsonParser(), defaultValue = null)

    @Provides
    @SingleIn(AppScope::class)
    fun provideAccessTokenDataStore(
        serializer: Serializer<AccessToken?>,
        produceFile: (String) -> File,
    ): DataStore<AccessToken?> =
        DataStoreFactory.create(serializer = serializer, produceFile = { produceFile(FILE_NAME) })

    companion object {
        private const val FILE_NAME = "accessToken.json"
    }
}
