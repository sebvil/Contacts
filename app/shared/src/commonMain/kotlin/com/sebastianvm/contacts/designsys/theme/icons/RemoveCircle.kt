package com.sebastianvm.contacts.designsys.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.RemoveCircle: ImageVector by
    lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(
                name = "RemoveCircle",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 960f,
                viewportHeight = 960f,
            )
            .apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(x = 280f, y = 520f)
                    horizontalLineToRelative(dx = 400f)
                    verticalLineToRelative(dy = -80f)
                    lineTo(x = 280f, y = 440f)
                    verticalLineToRelative(dy = 80f)
                    close()
                    moveTo(x = 480f, y = 880f)
                    quadToRelative(dx1 = -83f, dy1 = 0f, dx2 = -156f, dy2 = -31.5f)
                    reflectiveQuadTo(197f, 763f)
                    quadToRelative(dx1 = -54f, dy1 = -54f, dx2 = -85.5f, dy2 = -127f)
                    reflectiveQuadTo(80f, 480f)
                    quadToRelative(dx1 = 0f, dy1 = -83f, dx2 = 31.5f, dy2 = -156f)
                    reflectiveQuadTo(x1 = 197f, y1 = 197f)
                    quadToRelative(dx1 = 54f, dy1 = -54f, dx2 = 127f, dy2 = -85.5f)
                    reflectiveQuadTo(x1 = 480f, y1 = 80f)
                    quadToRelative(dx1 = 83f, dy1 = 0f, dx2 = 156f, dy2 = 31.5f)
                    reflectiveQuadTo(x1 = 763f, y1 = 197f)
                    quadToRelative(dx1 = 54f, dy1 = 54f, dx2 = 85.5f, dy2 = 127f)
                    reflectiveQuadTo(x1 = 880f, y1 = 480f)
                    quadToRelative(dx1 = 0f, dy1 = 83f, dx2 = -31.5f, dy2 = 156f)
                    reflectiveQuadTo(x1 = 763f, y1 = 763f)
                    quadToRelative(dx1 = -54f, dy1 = 54f, dx2 = -127f, dy2 = 85.5f)
                    reflectiveQuadTo(x1 = 480f, y1 = 880f)
                    close()
                    moveTo(x = 480f, y = 800f)
                    quadToRelative(dx1 = 134f, dy1 = 0f, dx2 = 227f, dy2 = -93f)
                    reflectiveQuadToRelative(dx1 = 93f, dy1 = -227f)
                    quadToRelative(dx1 = 0f, dy1 = -134f, dx2 = -93f, dy2 = -227f)
                    reflectiveQuadToRelative(dx1 = -227f, dy1 = -93f)
                    quadToRelative(dx1 = -134f, dy1 = 0f, dx2 = -227f, dy2 = 93f)
                    reflectiveQuadToRelative(dx1 = -93f, dy1 = 227f)
                    quadToRelative(dx1 = 0f, dy1 = 134f, dx2 = 93f, dy2 = 227f)
                    reflectiveQuadToRelative(dx1 = 227f, dy1 = 93f)
                    close()
                    moveTo(x = 480f, y = 480f)
                    close()
                }
            }
            .build()
    }
