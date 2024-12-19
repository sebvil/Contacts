package com.sebastianvm.contacts.model

import java.time.LocalDate

data class ContactWithBirthday(val name: String, val photoUri: String?, val birthday: LocalDate)
