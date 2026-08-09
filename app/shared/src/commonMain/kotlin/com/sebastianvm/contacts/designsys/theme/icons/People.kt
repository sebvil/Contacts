package com.sebastianvm.contacts.designsys.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.People: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "People",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
            .apply {
                path(fill = SolidColor(Color.Black)) {
                    // Back head.
                    moveTo(x = 16f, y = 11f)
                    curveToRelative(
                        dx1 = 1.66f,
                        dy1 = 0f,
                        dx2 = 2.99f,
                        dy2 = -1.34f,
                        dx3 = 2.99f,
                        dy3 = -3f,
                    )
                    reflectiveCurveTo(x1 = 17.66f, y1 = 5f, x2 = 16f, y2 = 5f)
                    curveToRelative(
                        dx1 = -1.66f,
                        dy1 = 0f,
                        dx2 = -3f,
                        dy2 = 1.34f,
                        dx3 = -3f,
                        dy3 = 3f,
                    )
                    reflectiveCurveToRelative(dx1 = 1.34f, dy1 = 3f, dx2 = 3f, dy2 = 3f)
                    close()

                    // Front head.
                    moveTo(x = 8f, y = 11f)
                    curveToRelative(
                        dx1 = 1.66f,
                        dy1 = 0f,
                        dx2 = 2.99f,
                        dy2 = -1.34f,
                        dx3 = 2.99f,
                        dy3 = -3f,
                    )
                    reflectiveCurveTo(x1 = 9.66f, y1 = 5f, x2 = 8f, y2 = 5f)
                    curveTo(x1 = 6.34f, y1 = 5f, x2 = 5f, y2 = 6.34f, x3 = 5f, y3 = 8f)
                    reflectiveCurveToRelative(dx1 = 1.34f, dy1 = 3f, dx2 = 3f, dy2 = 3f)
                    close()

                    // Front body.
                    moveTo(x = 8f, y = 13f)
                    curveToRelative(
                        dx1 = -2.33f,
                        dy1 = 0f,
                        dx2 = -7f,
                        dy2 = 1.17f,
                        dx3 = -7f,
                        dy3 = 3.5f,
                    )
                    verticalLineTo(y = 19f)
                    horizontalLineToRelative(dx = 14f)
                    verticalLineToRelative(dy = -2.5f)
                    curveToRelative(
                        dx1 = 0f,
                        dy1 = -2.33f,
                        dx2 = -4.67f,
                        dy2 = -3.5f,
                        dx3 = -7f,
                        dy3 = -3.5f,
                    )
                    close()

                    // Back body.
                    moveTo(x = 16f, y = 13f)
                    curveToRelative(
                        dx1 = -0.29f,
                        dy1 = 0f,
                        dx2 = -0.62f,
                        dy2 = 0.02f,
                        dx3 = -0.97f,
                        dy3 = 0.05f,
                    )
                    curveToRelative(
                        dx1 = 1.16f,
                        dy1 = 0.84f,
                        dx2 = 1.97f,
                        dy2 = 1.97f,
                        dx3 = 1.97f,
                        dy3 = 3.45f,
                    )
                    verticalLineTo(y = 19f)
                    horizontalLineToRelative(dx = 6f)
                    verticalLineToRelative(dy = -2.5f)
                    curveToRelative(
                        dx1 = 0f,
                        dy1 = -2.33f,
                        dx2 = -4.67f,
                        dy2 = -3.5f,
                        dx3 = -7f,
                        dy3 = -3.5f,
                    )
                    close()
                }
            }
            .build()
    }
