package com.sebastianvm.contacts.features.contacts.create

import com.sebastianvm.contacts.features.base.UiState

data class CreateContactState(
    val name: String,
    val isSaving: Boolean = false,
) : UiState
