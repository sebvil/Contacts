package com.sebastianvm.contacts.features.contacts.list

import com.sebastianvm.contacts.features.base.UiEvent
import kotlin.uuid.Uuid

sealed interface ContactListUiEvent : UiEvent {

    data object TryAgain : ContactListUiEvent

    data class ContactClicked(val contactId: Uuid) : ContactListUiEvent
}
