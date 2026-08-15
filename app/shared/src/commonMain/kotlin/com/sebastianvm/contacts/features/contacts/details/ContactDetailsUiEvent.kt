package com.sebastianvm.contacts.features.contacts.details

import com.sebastianvm.contacts.features.base.UiEvent

internal sealed interface ContactDetailsUiEvent : UiEvent {

    data object OnBackClicked : ContactDetailsUiEvent
}
