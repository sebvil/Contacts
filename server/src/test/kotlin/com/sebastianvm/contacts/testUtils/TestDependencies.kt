package com.sebastianvm.contacts.testUtils

import com.sebastianvm.contacts.di.TestAppGraph
import de.infix.testBalloon.framework.core.Test
import io.ktor.client.HttpClient

data class TestDependencies(
    val appGraph: TestAppGraph,
    val client: HttpClient,
    val testExecutionScope: Test.ExecutionScope,
)
