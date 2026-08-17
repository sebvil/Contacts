package com.sebastianvm.contacts.designsys.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import com.sebastianvm.contacts.designsys.theme.AppTheme
import com.sebastianvm.contacts.designsys.theme.Dimensions

@PreviewWrapper(ComponentPreviewWrapperProvider::class)
@PreviewLightDark
@PreviewFontScale
annotation class PreviewComponent

@PreviewLightDark @PreviewFontScale @PreviewScreenSizes annotation class PreviewScreens

class ComponentPreviewWrapperProvider : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        AppTheme {
            Surface(modifier = Modifier.padding(all = Dimensions.ScreenEdgePadding)) {
                content()
            }
        }
    }
}

// PreviewWrapper does not seem to work in Intellij yet, so need to keep this.
@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    ComponentPreviewWrapperProvider().Wrap { content() }
}
