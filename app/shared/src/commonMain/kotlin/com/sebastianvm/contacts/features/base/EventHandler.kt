package com.sebastianvm.contacts.features.base

public fun interface EventHandler<E : UiEvent> {
    public operator fun invoke(event: E)
}
