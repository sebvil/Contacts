package com.sebastianvm.contacts.app.database

import com.sebastianvm.contacts.domain.Contact
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface LocalContactsDataSource {

    public suspend fun insertContacts(contacts: List<Contact>)

    public fun getAllContacts(): Flow<List<Contact>>

    public fun getContact(id: Uuid): Flow<Contact>
}
