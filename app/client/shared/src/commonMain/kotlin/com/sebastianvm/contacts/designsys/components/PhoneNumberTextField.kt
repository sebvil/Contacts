package com.sebastianvm.contacts.designsys.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import contacts.app.client.shared.generated.resources.Res
import contacts.app.client.shared.generated.resources.country_colombia
import contacts.app.client.shared.generated.resources.country_us
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

object PhoneNumberTextField {

    @Composable
    operator fun invoke(
        textFieldState: TextFieldState,
        countryCode: CountryCode,
        onCountryCodeChange: (CountryCode) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        OutlinedTextField(
            state = textFieldState,
            modifier = modifier,
            inputTransformation = DigitOnlyInputTransformation(),
            leadingIcon = {
                CountryCode(
                    countryCode = countryCode,
                    onCountryCodeChange = onCountryCodeChange,
                )
            },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CountryCode(
        countryCode: CountryCode,
        onCountryCodeChange: (CountryCode) -> Unit,
    ) {
        var showCountryCodePicker by remember {
            mutableStateOf(false)
        }
        ExposedDropdownMenuBox(
            expanded = showCountryCodePicker,
            onExpandedChange = { showCountryCodePicker = it },
        ) {
            Box(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "${countryCode.emoji} +${countryCode.code}",
                    modifier =
                        Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable),
                )
            }

            DropdownMenu(
                expanded = showCountryCodePicker,
                onDismissRequest = { showCountryCodePicker = false },
            ) {
                CountryCode.entries.forEach { countryCodeOption ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text =
                                    "${countryCodeOption.emoji} ${stringResource(countryCodeOption.countryName)} +${countryCodeOption.code}"
                            )
                        },
                        onClick = {
                            onCountryCodeChange(countryCode)
                        },
                    )
                }
            }
        }
    }

    private class DigitOnlyInputTransformation : InputTransformation {
        override fun TextFieldBuffer.transformInput() {
            if (!asCharSequence().all { it.isDigit() }) {
                revertAllChanges()
            }
        }
    }
}

class PhoneNumberTextFieldPreviews {

    @PreviewComponent
    @Composable
    private fun EmptyFieldPreview() {
        PhoneNumberTextFieldPreview()
    }

    @PreviewComponent
    @Composable
    private fun WithNumberPreview() {
        PhoneNumberTextFieldPreview("(800) 560-4242")
    }

    @Composable
    private fun PhoneNumberTextFieldPreview(
        phoneNumber: String = "",
        countryCode: CountryCode = CountryCode.US,
    ) {
        PreviewWrapper {
            PhoneNumberTextField(
                textFieldState = rememberTextFieldState(phoneNumber),
                countryCode = countryCode,
                onCountryCodeChange = {},
            )
        }
    }
}

enum class CountryCode(val countryName: StringResource, val emoji: String, val code: Int) {
    US(countryName = Res.string.country_us, emoji = "🇺🇸", code = 1),
    Colombia(countryName = Res.string.country_colombia, emoji = "🇨🇴", code = 57),
}
