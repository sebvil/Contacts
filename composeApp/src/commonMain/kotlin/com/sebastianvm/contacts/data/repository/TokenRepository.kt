package com.sebastianvm.contacts.data.repository

import androidx.datastore.core.DataStore
import com.sebastianvm.contacts.model.AccessToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    suspend fun saveToken(token: AccessToken)

    fun getToken(): Flow<AccessToken?>

    suspend fun clearToken()
}

@ContributesBinding(AppScope::class)
class DatastoreTokenRepository(private val tokenDatastore: DataStore<AccessToken?>) :
    TokenRepository {

    override suspend fun saveToken(token: AccessToken) {
        tokenDatastore.updateData { token }
    }

    override fun getToken(): Flow<AccessToken?> {
        return tokenDatastore.data
    }

    override suspend fun clearToken() {
        tokenDatastore.updateData { null }
    }
}
