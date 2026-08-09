package com.sebastianvm.contacts.features.contacts.list

import com.sebastianvm.contacts.designsys.components.ContactListItem
import com.sebastianvm.contacts.features.base.UiState

sealed interface ContactListState : UiState {
    data object Idle : ContactListState

    data object Loading : ContactListState

    data object Error : ContactListState

    data class Data(val contacts: List<ContactListItem.State>) : ContactListState
}
