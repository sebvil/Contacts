package com.sebastianvm.contacts.navigation

import com.sebastianvm.contacts.mvvm.ContactsScreen
import com.sebastianvm.contacts.util.CommonParcelize

@CommonParcelize
data class NavHostScreen(val initialScreen: ContactsScreen, val showTopNavBar: Boolean = false) :
    ContactsScreen
