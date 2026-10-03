package com.sebastianvm.contacts.repository

import com.sebastianvm.contacts.database.tables.EmailAddressesTable
import com.sebastianvm.contacts.dto.EmailAddressResponse
import com.sebastianvm.core.types.Some
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toEmailAddressResponse(): EmailAddressResponse {
    return EmailAddressResponse(
        id = get(EmailAddressesTable.id).value,
        address = Some(get(EmailAddressesTable.address)),
        label = Some(get(EmailAddressesTable.label)),
    )
}
