package com.sebastianvm.watcher.data.repository

import com.sebastianvm.watcher.data.model.LoginState
import com.sebastianvm.watcher.model.AccessToken
import com.sebastianvm.watcher.routes.Login
import com.sebastianvm.watcher.routes.Register
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, replaces = [DefaultAuthRepository::class])
class FakeAuthRepository : AuthRepository {
    override val loginState: MutableStateFlow<LoginState?> = MutableStateFlow(null)

    private val users = mutableMapOf<String, String>()

    fun addUser(username: String, password: String) {
        users[username] = password
    }

    override suspend fun register(username: String, password: String): Register.Response {
        // Delay to simulate network latency, necessary for presenter tests
        delay(DELAY_MS)
        if (username in users.keys) return Register.Response.UserAlreadyExists
        users[username] = password
        return Register.Response.Success(AccessToken("fake-token", 1.minutes, "fake-refresh"))
    }

    override suspend fun logIn(username: String, password: String): Login.Response {
        delay(DELAY_MS)
        if (users[username] == password) {
            return Login.Response.Success(AccessToken("fake-token", 1.minutes, "fake-refresh"))
        }
        return Login.Response.InvalidCredentials
    }

    override suspend fun logOut() {
        loginState.value = LoginState.LoggedOut
    }

    companion object {
        private const val DELAY_MS = 1L
    }
}
