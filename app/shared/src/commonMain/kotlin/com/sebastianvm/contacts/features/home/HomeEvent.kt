package com.sebastianvm.contacts.features.home

import com.sebastianvm.contacts.features.base.UiEvent

internal sealed interface HomeEvent : UiEvent {
    data object AddContact : HomeEvent
}
