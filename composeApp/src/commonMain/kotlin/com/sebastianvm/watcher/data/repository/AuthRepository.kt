package com.sebastianvm.watcher.data.repository

import com.sebastianvm.watcher.data.model.LoginState
import com.sebastianvm.watcher.networking.post
import com.sebastianvm.watcher.routes.Login
import com.sebastianvm.watcher.routes.Logout
import com.sebastianvm.watcher.routes.Register
import com.sebastianvm.watcher.util.coroutines.stateIn
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface AuthRepository {

    val loginState: StateFlow<LoginState?>

    suspend fun register(username: String, password: String): Register.Response

    suspend fun logIn(username: String, password: String): Login.Response

    suspend fun logOut()
}

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class DefaultAuthRepository(
    appScope: CoroutineScope,
    private val tokenRepository: TokenRepository,
    private val client: HttpClient,
) : AuthRepository {

    override val loginState: StateFlow<LoginState?> =
        tokenRepository
            .getToken()
            .map { token -> if (token != null) LoginState.LoggedIn else LoggedOut }
            .stateIn(scope = appScope, initialValue = null)

    override suspend fun register(username: String, password: String): Register.Response {
        val result = client.post(route = Register, body = Register.Body(username, password))
        if (result is Register.Response.Success) {
            tokenRepository.saveToken(result.accessToken)
        }
        return result
    }

    override suspend fun logIn(username: String, password: String): Login.Response {
        val result = client.post(route = Login, body = Login.Body(username, password))
        if (result is Login.Response.Success) {
            tokenRepository.saveToken(result.accessToken)
        }
        return result
    }

    override suspend fun logOut() {
        val token = tokenRepository.getToken().firstOrNull() ?: return
        client.post(route = Logout, body = Logout.Body(refreshToken = token.refreshToken))
        tokenRepository.clearToken()
    }
}
