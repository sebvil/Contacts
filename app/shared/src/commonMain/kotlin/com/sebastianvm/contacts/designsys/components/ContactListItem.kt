package com.sebastianvm.contacts.designsys.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.components.ContactListItem.State
import kotlin.uuid.Uuid

object ContactListItem {

    data class State(val id: Uuid, val name: String)

    @Composable
    operator fun invoke(state: State, modifier: Modifier = Modifier) {
        ListItem(
            headlineContent = {
                Text(text = state.name)
            },
            modifier = modifier,
        )
    }
}

internal class PreviewsContainer {

    @PreviewComponent
    @Composable
    private fun Preview() {
        ComponentPreview { ContactListItem(State(Uuid.random(), "Elliot")) }
    }
}
