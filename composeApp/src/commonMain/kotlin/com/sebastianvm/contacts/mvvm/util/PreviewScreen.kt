package com.sebastianvm.contacts.mvvm.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import com.sebastianvm.contacts.designsys.theme.ContactsTheme
import com.sebastianvm.contacts.mvvm.Ui
import com.sebastianvm.contacts.mvvm.UiState

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@Preview(name = "Default", showSystemUi = true)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Large Font", showSystemUi = true, fontScale = 2.0f)
annotation class PreviewScreen

abstract class ScreenPreviews<S : UiState> {
    abstract val ui: Ui<S, *>

    @Composable
    fun Preview(state: S, modifier: Modifier = Modifier) {
        // False positive from detekt
        // Will be fixed in https://github.com/detekt/detekt/pull/9061
        @Suppress("UnnecessaryFullyQualifiedName")
        ContactsTheme { ui.Content(state = state, handle = {}, modifier = modifier) }
    }
}
