package com.sebastianvm.contacts.features.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.theme.icons.Icons
import com.sebastianvm.contacts.designsys.theme.icons.People
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.StaticUi
import com.sebastianvm.contacts.features.contacts.list.ContactListScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
import contacts.app.shared.generated.resources.Res
import contacts.app.shared.generated.resources.contacts_tab
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import org.jetbrains.compose.resources.stringResource

@CircuitInject(HomeScreen::class, AppScope::class)
@Inject
internal class HomeUi : StaticUi<HomeEvent>() {
    @Composable
    override fun Content(handleEvent: EventHandler<HomeEvent>, modifier: Modifier) {
        // A single "Contacts" tab today; the navigation suite adapts automatically between a
        // bottom nav bar, a nav rail, and a nav drawer depending on the available window size.
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                item(
                    selected = true,
                    onClick = {},
                    icon = { Icon(imageVector = Icons.People, contentDescription = null) },
                    label = { Text(text = stringResource(Res.string.contacts_tab)) },
                )
            },
            modifier = modifier,
        ) {
            CircuitContent(screen = ContactListScreen, modifier = Modifier.fillMaxSize())
        }
    }
}
