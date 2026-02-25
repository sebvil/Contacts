package com.sebastianvm.watcher.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sebastianvm.watcher.designsys.theme.Spacing
import com.sebastianvm.watcher.mvvm.Ui
import com.sebastianvm.watcher.mvvm.util.PreviewScreen
import com.sebastianvm.watcher.mvvm.util.ScreenPreviews
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import org.jetbrains.compose.resources.stringResource
import watcher.composeapp.generated.resources.Res
import watcher.composeapp.generated.resources.log_out
import watcher.composeapp.generated.resources.welcome_username

@CircuitInject(HomeScreen::class, AppScope::class)
class HomeUi : Ui<HomeState, HomeUserAction>() {

    @Composable
    override fun Content(state: HomeState, handle: (HomeUserAction) -> Unit, modifier: Modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(stringResource(Res.string.welcome_username, state.username))
            Spacer(Modifier.height(Spacing.Sm8))
            Button(
                onClick = { handle(HomeUserAction.LogOutClicked) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(Res.string.log_out))
            }
        }
    }
}

internal class HomePreviews : ScreenPreviews<HomeState>() {
    override val ui: Ui<HomeState, *>
        get() = HomeUi()

    @PreviewScreen
    @Composable
    internal fun DefaultState() {
        Preview(state = HomeState("Hello, friend"))
    }
}
