package com.sebastianvm.contacts.ui.mappers

import com.sebastianvm.contacts.designsys.components.ContactListItem
import com.sebastianvm.contacts.domain.Contact

internal fun Contact.toContactListItemState(): ContactListItem.State =
    ContactListItem.State(id = id, name = name)
