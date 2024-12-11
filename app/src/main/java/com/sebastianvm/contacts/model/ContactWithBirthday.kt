package com.sebastianvm.contacts.model

import kotlinx.datetime.LocalDate


data class ContactWithBirthday(val name: String, val photoUri: String?, val birthday: LocalDate)
