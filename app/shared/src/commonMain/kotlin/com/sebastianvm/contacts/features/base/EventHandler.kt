package com.sebastianvm.contacts.features.base

fun interface EventHandler<E : UiEvent> {
    operator fun invoke(event: E)
}
