package com.sebastianvm.watcher.features.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sebastianvm.watcher.mvvm.Ui
import com.slack.circuit.codegen.annotations.CircuitInject
import dev.zacsweers.metro.AppScope
import org.jetbrains.compose.resources.stringResource
import watcher.composeapp.generated.resources.Res
import watcher.composeapp.generated.resources.log_in
import watcher.composeapp.generated.resources.sign_up
import watcher.composeapp.generated.resources.welcome_to_watcher

@CircuitInject(LandingScreen::class, AppScope::class)
class LandingUi : Ui<LandingState, LandingUserAction>() {

    @Composable
    override fun Content(
        state: LandingState,
        handle: (LandingUserAction) -> Unit,
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = stringResource(Res.string.welcome_to_watcher))
            }
            Button(onClick = { handle(LoginClicked) }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(Res.string.log_in))
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(
                onClick = { handle(SignupClicked) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(Res.string.sign_up))
            }
        }
    }
}
