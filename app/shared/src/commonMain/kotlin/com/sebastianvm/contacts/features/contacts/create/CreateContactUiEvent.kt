package com.sebastianvm.contacts.features.contacts.create

import com.sebastianvm.contacts.features.base.UiEvent

sealed interface CreateContactUiEvent : UiEvent {
    data class NameChanged(val name: String) : CreateContactUiEvent

    data object SaveContact : CreateContactUiEvent

    data object BackClicked : CreateContactUiEvent
}
