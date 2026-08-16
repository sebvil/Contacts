package com.sebastianvm.contacts.designsys.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.MenuOpen: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "MenuOpen",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
            )
            .apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(120f, 720f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(520f)
                    verticalLineToRelative(80f)
                    lineTo(120f, 720f)
                    close()
                    moveTo(784f, 680f)
                    lineTo(584f, 480f)
                    lineToRelative(200f, -200f)
                    lineToRelative(56f, 56f)
                    lineToRelative(-144f, 144f)
                    lineToRelative(144f, 144f)
                    lineToRelative(-56f, 56f)
                    close()
                    moveTo(120f, 520f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(400f)
                    verticalLineToRelative(80f)
                    lineTo(120f, 520f)
                    close()
                    moveTo(120f, 320f)
                    verticalLineToRelative(-80f)
                    horizontalLineToRelative(520f)
                    verticalLineToRelative(80f)
                    lineTo(120f, 320f)
                    close()
                }
            }
            .build()
    }
