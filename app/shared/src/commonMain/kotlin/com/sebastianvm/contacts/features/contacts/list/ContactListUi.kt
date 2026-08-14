package com.sebastianvm.contacts.features.contacts.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sebastianvm.contacts.designsys.components.ContactListItem
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.Ui
import com.slack.circuit.codegen.annotations.CircuitInject
import contacts.app.shared.generated.resources.Res
import contacts.app.shared.generated.resources.contacts_tab_noun
import contacts.app.shared.generated.resources.something_went_wrong
import contacts.app.shared.generated.resources.try_again
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource

@CircuitInject(ContactListScreen::class, AppScope::class)
@Inject
internal class ContactListUi : Ui<ContactListState, ContactListUiEvent>() {
    @Composable
    override fun Content(
        state: ContactListState,
        handleEvent: EventHandler<ContactListUiEvent>,
        modifier: Modifier,
    ) {
        Scaffold(
            modifier.background(Color.Red),
            topBar = {
                TopAppBar(title = { Text(text = stringResource(Res.string.contacts_tab_noun)) })
            },
        ) { paddingValues ->
            val contentModifier = Modifier.fillMaxSize().padding(paddingValues)
            when (state) {
                ContactListState.Idle -> Unit
                is ContactListState.Data -> {
                    LazyColumn(modifier = contentModifier) {
                        items(state.contacts, key = { it.id }) { contact ->
                            ContactListItem(contact)
                        }
                    }
                }

                ContactListState.Error -> {
                    Column(
                        modifier = contentModifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(stringResource(Res.string.something_went_wrong))
                        Button(onClick = { handleEvent(ContactListUiEvent.TryAgain) }) {
                            Text(stringResource(Res.string.try_again))
                        }
                    }
                }

                ContactListState.Loading -> {
                    Box(
                        modifier = contentModifier,
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
