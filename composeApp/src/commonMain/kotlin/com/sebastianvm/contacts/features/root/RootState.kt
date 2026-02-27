package com.sebastianvm.contacts.features.root

import com.sebastianvm.contacts.mvvm.ContactsScreen
import com.sebastianvm.contacts.mvvm.UiState

data class RootState(val screen: ContactsScreen?) : UiState
