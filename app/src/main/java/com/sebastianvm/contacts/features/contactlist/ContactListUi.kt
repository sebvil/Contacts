package com.sebastianvm.contacts.features.contactlist

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.sebastianvm.contacts.model.SimpleContact
import com.sebastianvm.contacts.ui.theme.ContactsTheme

@Composable
fun ContactListUi(
    modifier: Modifier = Modifier,
    viewModel: ContactListViewModel =
        viewModel(factory = ContactListViewModel.Factory(LocalContext.current)),
) {
  ContactListUi(
      contacts = viewModel.contacts.value,
      loadContacts = { viewModel.loadContacts() },
      modifier = modifier)
}

@Composable
fun ContactListUi(contacts: List<SimpleContact>, loadContacts: () -> Unit, modifier: Modifier) {

  var hasContactsPermissions by remember { mutableStateOf(false) }
  val context = LocalContext.current

  val permissionLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
          hasContactsPermissions = true
          loadContacts()
        } else {
          hasContactsPermissions = false
        }
      }
  LaunchedEffect(hasContactsPermissions) {
    if (hasContactsPermissions) {
      loadContacts()
      return@LaunchedEffect
    }

    val permissionState =
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)

    if (permissionState == PackageManager.PERMISSION_GRANTED) {
      hasContactsPermissions = true
      return@LaunchedEffect
    }

    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
  }
  Scaffold(modifier = modifier) { innerPadding ->
    if (!hasContactsPermissions) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { permissionLauncher.launch(Manifest.permission.READ_CONTACTS) }) {
          Text(text = "Grant contacts permission")
        }
      }
    } else {
      LazyColumn(Modifier.padding(innerPadding)) {
        items(contacts) { Contact(name = it.name, it.photoUri) {} }
      }
    }
  }
}

@Composable
fun Contact(name: String, photoUri: String?, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
      headlineContent = { Text(text = name) },
      modifier = modifier.clickable { onClick() })
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Preview
@Composable
fun ContactPreview() {
  ContactsTheme { Contact(name = "Sebastian Villegas", null) {} }
}
