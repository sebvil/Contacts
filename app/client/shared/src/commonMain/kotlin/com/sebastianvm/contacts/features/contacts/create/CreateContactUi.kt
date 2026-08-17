package com.sebastianvm.contacts.features.contacts.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sebastianvm.contacts.designsys.components.CountryCode
import com.sebastianvm.contacts.designsys.components.PhoneNumberTextField
import com.sebastianvm.contacts.designsys.theme.icons.ArrowBack
import com.sebastianvm.contacts.designsys.theme.icons.Icons
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.Ui
import com.sebastianvm.contacts.ui.fillConstrainedMaxWidth
import com.slack.circuit.codegen.annotations.CircuitInject
import contacts.app.client.shared.generated.resources.Res
import contacts.app.client.shared.generated.resources.back_noun
import contacts.app.client.shared.generated.resources.create_contact
import contacts.app.client.shared.generated.resources.name_noun
import contacts.app.client.shared.generated.resources.save_verb
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource

@CircuitInject(CreateContactScreen::class, AppScope::class)
@Inject
internal class CreateContactUi : Ui<CreateContactState, CreateContactUiEvent>() {
    @Composable
    override fun Content(
        state: CreateContactState,
        handleEvent: EventHandler<CreateContactUiEvent>,
        modifier: Modifier,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(Res.string.create_contact)) },
                    navigationIcon = {
                        IconButton(onClick = { handleEvent(CreateContactUiEvent.BackClicked) }) {
                            Icon(
                                Icons.ArrowBack,
                                contentDescription = stringResource(Res.string.back_noun),
                            )
                        }
                    },
                    contentPadding = PaddingValues(end = 12.dp),
                    actions = {
                        Button(
                            onClick = { handleEvent(CreateContactUiEvent.SaveContact) },
                            enabled = state.name.isNotBlank() && !state.isSaving,
                        ) {
                            Text(stringResource(Res.string.save_verb))
                        }
                    },
                )
            },
        ) { paddingValues ->
            Column(
                modifier =
                    Modifier.fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (state.isSaving) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { handleEvent(CreateContactUiEvent.NameChanged(it)) },
                    label = { Text(stringResource(Res.string.name_noun)) },
                    modifier = Modifier.fillConstrainedMaxWidth(),
                    enabled = !state.isSaving,
                    singleLine = true,
                )

                val textFieldState = rememberTextFieldState()
                var countryCode by remember {
                    mutableStateOf(CountryCode.US)
                }
                PhoneNumberTextField(
                    textFieldState = textFieldState,
                    countryCode = countryCode,
                    onCountryCodeChange = { countryCode = it },
                    modifier = Modifier.fillConstrainedMaxWidth(),
                )
            }
        }
    }
}
