package com.sebastianvm.contacts.features.contacts.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.theme.Dimensions
import com.sebastianvm.contacts.designsys.theme.icons.ArrowBack
import com.sebastianvm.contacts.designsys.theme.icons.Icons
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.Ui
import com.slack.circuit.codegen.annotations.CircuitInject
import contacts.app.shared.generated.resources.Res
import contacts.app.shared.generated.resources.back_noun
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource

@CircuitInject(ContactDetailsScreen::class, AppScope::class)
@Inject
internal class ContactDetailsUi : Ui<ContactDetailsState, ContactDetailsUiEvent>() {
    @Composable
    override fun Content(
        state: ContactDetailsState,
        handleEvent: EventHandler<ContactDetailsUiEvent>,
        modifier: Modifier,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { handleEvent(ContactDetailsUiEvent.OnBackClicked) }) {
                            Icon(
                                Icons.ArrowBack,
                                contentDescription = stringResource(Res.string.back_noun),
                            )
                        }
                    },
                )
            },
        ) { padding ->
            LazyColumn(
                modifier =
                    Modifier.padding(padding).padding(horizontal = Dimensions.ScreenEdgePadding)
            ) {
                item {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(state.name, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}
