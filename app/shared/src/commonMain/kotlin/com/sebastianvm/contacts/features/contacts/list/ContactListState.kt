package com.sebastianvm.contacts.features.contacts.list

import com.sebastianvm.contacts.designsys.components.ContactListItem
import com.sebastianvm.contacts.features.base.UiState

internal sealed interface ContactListState : UiState {
    data object Loading : ContactListState

    data object Error : ContactListState

    data class Data(val contacts: List<ContactListItem.State>) : ContactListState
}
