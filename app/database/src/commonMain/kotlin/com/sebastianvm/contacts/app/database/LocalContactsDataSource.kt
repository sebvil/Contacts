package com.sebastianvm.contacts.app.database

import com.sebastianvm.contacts.domain.Contact
import kotlinx.coroutines.flow.Flow

public interface LocalContactsDataSource {

    public suspend fun insertContacts(contacts: List<Contact>)

    public fun getAllContacts(): Flow<List<Contact>>
}
