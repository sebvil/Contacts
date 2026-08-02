package com.sebastianvm.contacts.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import com.sebastianvm.contacts.designsys.theme.Dimensions

public fun Modifier.fillButtonMaxWidth(): Modifier =
    widthIn(max = Dimensions.MaxButtonWidth).fillMaxWidth()
