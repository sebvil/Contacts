package com.sebastianvm.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.features.birthdaylist.BirthdayListUi
import com.sebastianvm.contacts.ui.theme.ContactsTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { ContactsTheme { BirthdayListUi(modifier = Modifier.fillMaxSize()) } }
  }
}
