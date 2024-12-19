package com.sebastianvm.contacts.data

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.sebastianvm.contacts.model.ContactWithBirthday
import com.sebastianvm.contacts.model.SimpleContact
import kotlinx.datetime.LocalDate
import java.time.MonthDay
import java.time.format.DateTimeFormatter

class ContactsRepository(private val context: Context) {

  fun getContacts(): List<SimpleContact> {
    val contentResolver = context.contentResolver
    val projection =
        arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

    val cursor =
        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, projection, null, null, "display_name ASC")

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

  fun getBirthdays(): List<ContactWithBirthday> {
    val contentResolver: ContentResolver = context.contentResolver
    val contactsList = mutableListOf<ContactWithBirthday>()

    val projection =
        arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

    val cursor =
        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, projection, null, null, "display_name ASC")

    cursor?.use {
      while (it.moveToNext()) {
        val contactId = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
        val name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
        val photoUri =
            it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
        val birthday = getContactBirthday(contactId)

        if (birthday != null) {
            contactsList.add(
                ContactWithBirthday(
                    name,
                    photoUri = photoUri,
                    birthday = LocalDate.parse(birthday)
                )
            )
        }
      }
    }

    return contactsList
  }

  private fun getContactBirthday(contactId: String): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val birthdayCursor =
        contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Event.START_DATE),
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(contactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
            null)

    birthdayCursor?.use { cursor ->
      if (cursor.moveToFirst()) {
        return cursor.getString(
            cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Event.START_DATE))
      }
    }
    return null
  }
}
