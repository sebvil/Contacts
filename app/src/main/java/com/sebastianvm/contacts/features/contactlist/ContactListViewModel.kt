package com.sebastianvm.contacts.features.contactlist

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.sebastianvm.contacts.data.ContactsRepository
import com.sebastianvm.contacts.model.SimpleContact

class ContactListViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {

  private val _contacts: MutableState<List<SimpleContact>> = mutableStateOf(emptyList())
  val contacts: State<List<SimpleContact>> = _contacts

  fun loadContacts() {
    _contacts.value = contactsRepository.getContacts()
  }

  class Factory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
      return ContactListViewModel(contactsRepository = ContactsRepository(context)) as T
    }
  }
}
