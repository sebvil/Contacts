package com.sebastianvm.contacts.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.sebastianvm.contacts.designsys.theme.Dimensions
import com.sebastianvm.contacts.designsys.theme.icons.Add
import com.sebastianvm.contacts.designsys.theme.icons.Icons
import com.sebastianvm.contacts.designsys.theme.icons.Menu
import com.sebastianvm.contacts.designsys.theme.icons.MenuOpen
import com.sebastianvm.contacts.designsys.theme.icons.People
import com.sebastianvm.contacts.features.base.EventHandler
import com.sebastianvm.contacts.features.base.StaticUi
import com.sebastianvm.contacts.features.contacts.list.ContactListScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
import contacts.app.shared.generated.resources.Res
import contacts.app.shared.generated.resources.add_contact
import contacts.app.shared.generated.resources.collapse_rail
import contacts.app.shared.generated.resources.collapsed
import contacts.app.shared.generated.resources.contacts_tab
import contacts.app.shared.generated.resources.expand_rail
import contacts.app.shared.generated.resources.expanded
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@CircuitInject(HomeScreen::class, AppScope::class)
@Inject
internal class HomeUi : StaticUi<HomeEvent>() {
    @Composable
    override fun Content(handleEvent: EventHandler<HomeEvent>, modifier: Modifier) {
        val navigationRailState = rememberWideNavigationRailState()
        val adaptiveWindowInfo = currentWindowAdaptiveInfoV2()

        val navigationType: NavigationType =
            remember(adaptiveWindowInfo) {
                with(adaptiveWindowInfo) {
                    if (
                        !windowPosture.isTabletop &&
                            windowSizeClass.isAtLeastBreakpoint(
                                widthDpBreakpoint = WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND,
                                heightDpBreakpoint = WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND,
                            )
                    ) {
                        NavigationType.NavigationRail(navigationRailState = navigationRailState)
                    } else if (
                        windowPosture.isTabletop ||
                            windowSizeClass.isWidthAtLeastBreakpoint(
                                WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
                            )
                    ) {
                        NavigationType.NavigationBar(isCompact = false)
                    } else {
                        NavigationType.NavigationBar(isCompact = true)
                    }
                }
            }

        NavigationSuite(
            navigationType,
            fab = { isExpanded ->
                AddContactButton(isExpanded)
            },
            navigationIcons = {
                NavigationItem(
                    navigationType = navigationType,
                    selected = true,
                    icon = Icons.People,
                    text = stringResource(Res.string.contacts_tab),
                )
            },
        ) { padding ->
            CircuitContent(
                screen = ContactListScreen,
                modifier = Modifier.fillMaxSize().padding(padding),
            )
        }
    }

    @Composable
    private fun NavigationSuite(
        navigationType: NavigationType,
        fab: @Composable (isExpanded: Boolean) -> Unit,
        navigationIcons: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        val moveableNavigationIcons = remember {
            movableContentOf { navigationIcons() }
        }

        val moveableFab = remember {
            movableContentOf<Boolean> { fab(it) }
        }

        val moveableContent = remember {
            movableContentOf<PaddingValues> { content(it) }
        }

        when (navigationType) {
            is NavigationType.NavigationRail -> {
                NavigationRailLayout(
                    navigationRailState = navigationType.navigationRailState,
                    fab = {
                        moveableFab(
                            navigationType.navigationRailState.targetValue ==
                                WideNavigationRailValue.Expanded
                        )
                    },
                    navigationIcons = moveableNavigationIcons,
                    modifier = modifier,
                    content = {
                        moveableContent(PaddingValues())
                    },
                )
            }

            is NavigationType.NavigationBar -> {
                NavigationBarLayout(
                    navigationIcons = moveableNavigationIcons,
                    modifier = modifier,
                    fab = { moveableFab(true) },
                    content = { padding ->
                        moveableContent(padding)
                    },
                )
            }
        }
    }

    @Composable
    private fun NavigationRailLayout(
        navigationRailState: WideNavigationRailState,
        fab: @Composable () -> Unit,
        navigationIcons: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    ) {
        Row(modifier = modifier) {
            NavigationRail(
                navigationRailState = navigationRailState,
                fab = fab,
                navigationIcons = navigationIcons,
            )
            content()
        }
    }

    @Composable
    private fun NavigationBarLayout(
        fab: @Composable () -> Unit,
        navigationIcons: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                NavigationBar { navigationIcons() }
            },
            floatingActionButton = fab,
        ) { padding ->
            content(padding)
        }
    }

    @Composable
    private fun NavigationRail(
        navigationRailState: WideNavigationRailState,
        fab: @Composable () -> Unit,
        navigationIcons: @Composable () -> Unit,
    ) {

        WideNavigationRail(
            state = navigationRailState,
            header = {
                Column(Modifier.padding(start = 20.dp)) {
                    MenuButton(navigationRailState)
                    Spacer(modifier = Modifier.height(Dimensions.ListItemSpacing))
                    fab()
                }
            },
        ) {
            navigationIcons()
        }
    }

    @Composable
    private fun MenuButton(navigationRailState: WideNavigationRailState) {
        val scope = rememberCoroutineScope()
        val headerDescription =
            if (navigationRailState.targetValue == WideNavigationRailValue.Expanded) {
                stringResource(Res.string.collapse_rail)
            } else {
                stringResource(Res.string.expand_rail)
            }
        val stateDescription =
            if (navigationRailState.currentValue == WideNavigationRailValue.Expanded) {
                stringResource(Res.string.expanded)
            } else {
                stringResource(Res.string.collapsed)
            }
        IconButton(
            modifier =
                Modifier.padding(start = 4.dp).semantics {
                    // The button must announce the expanded or collapsed state of the
                    // rail for accessibility.
                    this.stateDescription = stateDescription
                },
            onClick = {
                scope.launch {
                    if (navigationRailState.targetValue == WideNavigationRailValue.Expanded)
                        navigationRailState.collapse()
                    else navigationRailState.expand()
                }
            },
        ) {
            if (navigationRailState.targetValue == WideNavigationRailValue.Expanded) {
                Icon(Icons.MenuOpen, headerDescription)
            } else {
                Icon(Icons.Menu, headerDescription)
            }
        }
    }

    @Composable
    private fun NavigationBar(navigationIcons: @Composable () -> Unit) {
        ShortNavigationBar {
            navigationIcons()
        }
    }

    @Composable
    private fun AddContactButton(isExpanded: Boolean) {
        if (isExpanded) {
            ExtendedFloatingActionButton(
                onClick = {},
                icon = {
                    Icon(Icons.Add, null)
                },
                text = {
                    Text(stringResource(Res.string.add_contact))
                },
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            )
        } else {
            FloatingActionButton(
                onClick = {},
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
                Icon(Icons.Add, stringResource(Res.string.add_contact))
            }
        }
    }

    @Composable
    private fun NavigationItem(
        navigationType: NavigationType,
        selected: Boolean,
        icon: ImageVector,
        text: String,
        modifier: Modifier = Modifier,
    ) {
        when (navigationType) {
            is NavigationType.NavigationRail -> {
                WideNavigationRailItem(
                    selected = selected,
                    onClick = {},
                    icon = { Icon(imageVector = icon, contentDescription = null) },
                    label = { Text(text = text) },
                    railExpanded =
                        navigationType.navigationRailState.targetValue ==
                            WideNavigationRailValue.Expanded,
                    modifier = modifier,
                )
            }

            is NavigationType.NavigationBar -> {
                ShortNavigationBarItem(
                    selected = selected,
                    onClick = {},
                    icon = { Icon(imageVector = icon, contentDescription = null) },
                    label = { Text(text = text) },
                    modifier = modifier,
                    iconPosition =
                        if (navigationType.isCompact) NavigationItemIconPosition.Top
                        else NavigationItemIconPosition.Start,
                )
            }
        }
    }

    sealed interface NavigationType {
        data class NavigationRail(val navigationRailState: WideNavigationRailState) : NavigationType

        data class NavigationBar(val isCompact: Boolean) : NavigationType
    }
}
