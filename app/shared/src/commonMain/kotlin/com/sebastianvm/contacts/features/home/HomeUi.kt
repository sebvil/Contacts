package com.sebastianvm.contacts.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.theme.Dimensions
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.StaticUi
import com.slack.circuit.codegen.annotations.CircuitInject
import contacts.app.shared.generated.resources.Res
import contacts.app.shared.generated.resources.app_name
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource

@CircuitInject(HomeScreen::class, AppScope::class)
@Inject
internal class HomeUi : StaticUi<HomeEvent>() {
    @Composable
    override fun Content(handleEvent: EventHandler<HomeEvent>, modifier: Modifier) {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(all = Dimensions.ScreenEdgePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(
                    Dimensions.ListItemSpacing,
                    alignment = Alignment.CenterVertically,
                ),
        ) {
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}
