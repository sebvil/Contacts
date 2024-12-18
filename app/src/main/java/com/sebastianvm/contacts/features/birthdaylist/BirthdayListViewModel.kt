package com.sebastianvm.contacts.features.birthdaylist

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.model.ContactWithBirthday
import java.time.LocalDate
import java.time.MonthDay

class BirthdayListViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {

  private val _contacts: MutableState<Map<LocalDate, List<ContactWithBirthday>>> =
      mutableStateOf(emptyMap())
  val contacts: State<Map<LocalDate, List<ContactWithBirthday>>> = _contacts

  fun loadContacts() {
    val today = LocalDate.now()
    _contacts.value =
        contactsRepository
            .getBirthdays()
            .map {
              val date = it.birthday
              val anniversaryThisYear = MonthDay.from(date).atYear(today.year)
              if (anniversaryThisYear.isBefore(today)) {
                anniversaryThisYear.plusYears(1) to it
              } else {
                // Anniversary is upcoming this year, calculate days until then
                anniversaryThisYear to it
              }
            }
            .groupBy({ it.first }) { it.second }.toSortedMap()
  }

  class Factory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
      return BirthdayListViewModel(contactsRepository = ContactsRepository(context)) as T
    }
  }
}
