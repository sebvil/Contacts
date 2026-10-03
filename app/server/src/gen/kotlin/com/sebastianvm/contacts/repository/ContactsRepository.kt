package com.sebastianvm.contacts.repository

import com.sebastianvm.contacts.database.tables.ContactsTable
import com.sebastianvm.contacts.dto.ContactResponse
import com.sebastianvm.contacts.dto.EmailAddressResponse
import com.sebastianvm.core.types.Option
import com.sebastianvm.core.types.Some
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toContactResponse(
    emailAddresses: Option<List<EmailAddressResponse>>
): ContactResponse {
    return ContactResponse(
        id = get(ContactsTable.id).value,
        name = Some(get(ContactsTable.name)),
        birthday = Some(get(ContactsTable.birthday)),
        emailAddresses = emailAddresses,
    )
}
