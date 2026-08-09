package com.sebastianvm.contacts.features.contacts.list

import com.sebastianvm.contacts.features.base.UiEvent

sealed interface ContactListUiEvent : UiEvent {

    data object TryAgain : ContactListUiEvent
}
