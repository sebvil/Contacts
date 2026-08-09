package com.sebastianvm.contacts.designsys.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
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
            .addPath(
                pathData = PathParser().parsePathString(PEOPLE_PATH_DATA).toNodes(),
                fill = SolidColor(Color.Black),
            )
            .build()
    }

private const val PEOPLE_PATH_DATA =
    "M16,11c1.66,0 2.99,-1.34 2.99,-3S17.66,5 16,5c-1.66,0 -3,1.34 -3,3s1.34,3 3,3zM8,11c1.66,0 " +
        "2.99,-1.34 2.99,-3S9.66,5 8,5C6.34,5 5,6.34 5,8s1.34,3 3,3zM8,13c-2.33,0 -7,1.17 -7,3.5V19h14v-2.5c0,-2.33 " +
        "-4.67,-3.5 -7,-3.5zM16,13c-0.29,0 -0.62,0.02 -0.97,0.05 1.16,0.84 1.97,1.97 1.97,3.45V19h6v-2.5c0,-2.33 " +
        "-4.67,-3.5 -7,-3.5z"
