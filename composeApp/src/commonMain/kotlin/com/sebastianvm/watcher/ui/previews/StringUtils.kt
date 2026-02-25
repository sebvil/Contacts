package com.sebastianvm.watcher.ui.previews

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

val LongString
    get() = LoremIpsum(LOREM_IPSUM_LENGTH).values.joinToString("")

private const val LOREM_IPSUM_LENGTH = 10
