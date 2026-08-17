package com.sebastianvm.contacts.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.theme.Dimensions

fun Modifier.fillConstrainedMaxWidth(): Modifier =
    widthIn(max = Dimensions.ConstrainedMaxWidth).fillMaxWidth()
