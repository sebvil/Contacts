package com.sebastianvm.contacts.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.sebastianvm.contacts.data.repository.AuthRepository
import com.sebastianvm.contacts.mvvm.ContactsPresenter
import com.sebastianvm.contacts.mvvm.ScreenState
import com.sebastianvm.contacts.networking.get
import com.sebastianvm.contacts.routes.Home
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@CircuitInject(screen = HomeScreen::class, scope = AppScope::class)
@Inject
class HomePresenter(private val client: HttpClient, private val authRepository: AuthRepository) :
    ContactsPresenter<HomeState, HomeUserAction> {
    @Composable
    override fun present(): ScreenState<HomeState, HomeUserAction> {
        // This should really be calling a repository method, but this is a temporary state just to
        // get a logged-in screen, so not bothering with the extra architecture for now
        val username =
            flow {
                    val response = client.get(Home) as? Home.Response.Success
                    emit(response?.username)
                }
                .collectAsState(null)
        val coroutineScope = rememberCoroutineScope()
        return ScreenState(HomeState(username.value.orEmpty())) { action ->
            when (action) {
                is HomeUserAction.LogOutClicked -> coroutineScope.launch { authRepository.logOut() }
            }
        }
    }
}
