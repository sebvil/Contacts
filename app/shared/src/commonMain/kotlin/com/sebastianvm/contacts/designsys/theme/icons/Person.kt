package com.sebastianvm.contacts.designsys.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.Person: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "Person",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
            .apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(x = 12f, y = 12f)
                    curveToRelative(
                        dx1 = 2.21f,
                        dy1 = 0f,
                        dx2 = 4f,
                        dy2 = -1.79f,
                        dx3 = 4f,
                        dy3 = -4f,
                    )
                    reflectiveCurveToRelative(dx1 = -1.79f, dy1 = -4f, dx2 = -4f, dy2 = -4f)
                    reflectiveCurveToRelative(dx1 = -4f, dy1 = 1.79f, dx2 = -4f, dy2 = 4f)
                    reflectiveCurveToRelative(dx1 = 1.79f, dy1 = 4f, dx2 = 4f, dy2 = 4f)
                    close()
                    moveToRelative(dx = 0f, dy = 2f)
                    curveToRelative(
                        dx1 = -2.67f,
                        dy1 = 0f,
                        dx2 = -8f,
                        dy2 = 1.34f,
                        dx3 = -8f,
                        dy3 = 4f,
                    )
                    verticalLineToRelative(dy = 2f)
                    horizontalLineToRelative(dx = 16f)
                    verticalLineToRelative(dy = -2f)
                    curveToRelative(
                        dx1 = 0f,
                        dy1 = -2.66f,
                        dx2 = -5.33f,
                        dy2 = -4f,
                        dx3 = -8f,
                        dy3 = -4f,
                    )
                    close()
                }
            }
            .build()
    }
