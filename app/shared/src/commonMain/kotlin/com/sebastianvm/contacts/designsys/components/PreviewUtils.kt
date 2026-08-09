package com.sebastianvm.contacts.designsys.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.sebastianvm.contacts.designsys.theme.AppTheme
import com.sebastianvm.contacts.designsys.theme.Dimensions

@PreviewLightDark @PreviewFontScale annotation class PreviewComponent

@PreviewLightDark @PreviewFontScale @PreviewScreenSizes annotation class PreviewScreens

@Composable
fun ComponentPreview(modifier: Modifier = Modifier, content: @Composable (() -> Unit)) {
    AppTheme {
        Surface(modifier = modifier.padding(all = Dimensions.ScreenEdgePadding)) {
            content()
        }
    }
}
