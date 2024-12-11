package com.sebastianvm.contacts.util

import java.time.LocalDate
import java.time.MonthDay

fun findNextAnniversary(date: MonthDay): LocalDate {
  val today = LocalDate.now()
  val anniversaryThisYear = MonthDay.from(date).atYear(today.year)
  return if (anniversaryThisYear.isBefore(today)) {
    anniversaryThisYear.plusYears(1)
  } else {
    // Anniversary is upcoming this year, calculate days until then
    anniversaryThisYear
  }
}
