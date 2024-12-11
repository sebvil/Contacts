package com.sebastianvm.contacts.features.birthdaylist

import android.Manifest
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.sebastianvm.contacts.model.ContactWithBirthday
import com.sebastianvm.contacts.ui.theme.ContactsTheme
import com.sebastianvm.contacts.ui.util.permissions.rememberPermissionHandler
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun BirthdayListUi(
    modifier: Modifier = Modifier,
    viewModel: BirthdayListViewModel =
        viewModel(factory = BirthdayListViewModel.Factory(LocalContext.current)),
) {
  BirthdayListUi(
      birthdays = viewModel.contacts.value,
      loadBirthdays = { viewModel.loadContacts() },
      modifier = modifier)
}

@Composable
fun BirthdayListUi(
    birthdays: Map<LocalDate, List<ContactWithBirthday>>,
    loadBirthdays: () -> Unit,
    modifier: Modifier
) {

  val contactsPermissionHandler =
      rememberPermissionHandler(Manifest.permission.READ_CONTACTS) { loadBirthdays() }

  Scaffold(modifier = modifier) { innerPadding ->
    if (!contactsPermissionHandler.hasPermission) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { contactsPermissionHandler.requestPermission() }) {
          Text(text = "Grant contacts permission")
        }
      }
    } else {
      LazyColumn(Modifier.padding(innerPadding)) {
        birthdays.forEach { (date, contacts) ->
          item {
            ListItem(
                headlineContent = {
                  Text(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
                })
          }
          items(contacts) {
            Birthday(
                name = it.name,
                it.photoUri,
                it.birthday.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))) {}
          }
        }
      }
    }
  }
}

@Composable
fun Birthday(
    name: String,
    photoUri: String?,
    birthday: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
  ListItem(
      leadingContent = {
        Box(
            modifier =
                Modifier.size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center) {
              if (photoUri != null) {
                AsyncImage(
                    model = photoUri, contentDescription = null, modifier = Modifier.fillMaxSize())
              } else {
                Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer))
              }
            }
      },
      supportingContent = { Text(birthday) },
      headlineContent = { Text(text = name) },
      modifier = modifier.clickable { onClick() })
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview
@Composable
fun BirthdayPreview() {
  ContactsTheme { Birthday(name = "Tony Stark", null, "1970-01-01") {} }
}
