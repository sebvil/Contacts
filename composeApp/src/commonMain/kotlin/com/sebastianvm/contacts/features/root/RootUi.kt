package com.sebastianvm.contacts.features.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.mvvm.Ui
import com.sebastianvm.contacts.mvvm.util.PreviewScreen
import com.sebastianvm.contacts.mvvm.util.ScreenPreviews
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
import contacts.composeapp.generated.resources.Res
import contacts.composeapp.generated.resources.app_name
import dev.zacsweers.metro.AppScope
import org.jetbrains.compose.resources.stringResource

@CircuitInject(RootScreen::class, AppScope::class)
class RootUi : Ui<RootState, Nothing>() {

    @Composable
    override fun Content(state: RootState, handle: (Nothing) -> Unit, modifier: Modifier) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.screen == null) {
                Text(stringResource(Res.string.app_name))
            } else {
                CircuitContent(screen = state.screen, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

internal class RootPreviews : ScreenPreviews<RootState>() {
    override val ui: Ui<RootState, *>
        get() = RootUi()

    @PreviewScreen
    @Composable
    internal fun Default() {
        Preview(state = RootState(screen = null))
    }
}
