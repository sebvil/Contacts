package com.sebastianvm.contacts.testutils

import kotlin.time.Clock
import kotlin.time.Instant

class FakeClock : Clock {
    var currentTime = Clock.System.now()

    override fun now(): Instant {
        return currentTime
    }
}
