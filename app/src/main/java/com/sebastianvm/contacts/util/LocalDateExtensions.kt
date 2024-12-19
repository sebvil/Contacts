package com.sebastianvm.contacts.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

fun LocalDate.copy(year: Int = this.year, month: Month = this.month, dayOfMonth: Int = this.dayOfMonth): LocalDate {
    return LocalDate(year = year, month = month, dayOfMonth = dayOfMonth)
}
