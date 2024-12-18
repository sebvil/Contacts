package com.sebastianvm.contacts.data

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import com.sebastianvm.contacts.model.SimpleContact

class ContactsRepository(private val context: Context) {

  fun getContacts(): List<SimpleContact> {
    val contentResolver = context.contentResolver
    val projection =
        arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

    val cursor =
        contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, "display_name ASC")

    if (cursor != null && cursor.moveToFirst()) {
      val contacts = mutableListOf<SimpleContact>()
      val nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
      val photoColumn = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
      do {
        val name: String = cursor.getString(nameColumn).orEmpty()
        val photoUri: String? = cursor.getString(photoColumn)
        contacts.add(SimpleContact(name, photoUri))
      } while (cursor.moveToNext())
      cursor.close()
      return contacts
    }
    return emptyList()
  }
}
