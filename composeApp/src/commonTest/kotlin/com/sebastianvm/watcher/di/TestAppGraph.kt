package com.sebastianvm.watcher.di

import androidx.datastore.core.DataStore
import com.sebastianvm.watcher.data.repository.FakeAuthRepository
import com.sebastianvm.watcher.data.repository.TokenRepository
import com.sebastianvm.watcher.model.AccessToken
import com.sebastianvm.watcher.util.coroutines.AppScopeProvider
import de.infix.testBalloon.framework.core.Test
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.TestFixture
import de.infix.testBalloon.framework.core.TestSuiteScope
import de.infix.testBalloon.framework.shared.TestDisplayName
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

@DependencyGraph(AppScope::class, excludes = [AppScopeProvider::class])
interface TestAppGraph : AppGraph {

    val tokenDataStore: DataStore<AccessToken?>
    val authRepository: FakeAuthRepository
    val tokenRepository: TokenRepository
    val testScope: CoroutineScope
    val httpClient: HttpClient

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Provides testScope: CoroutineScope): TestAppGraph
    }
}

@TestRegistering
fun TestSuiteScope.testWithDependencies(
    @TestElementName name: String,
    @TestDisplayName displayName: String = name,
    testConfig: TestConfig = TestConfig,
    action:
        suspend context(TestAppGraph)
        Test.ExecutionScope.() -> Unit,
) {

    test(name, displayName, testConfig) {
        val graph =
            createGraphFactory<TestAppGraph.Factory>().create(testScope = testScope.backgroundScope)
        context(graph) { action() }
    }
}

@TestRegistering
fun <Value> TestFixture.Scope<suspend Test.ExecutionScope.(Value) -> Unit>.testWithDependencies(
    @TestElementName name: String,
    @TestDisplayName displayName: String = name,
    testConfig: TestConfig = TestConfig,
    fixtureScopedAction:
        suspend context(TestAppGraph)
        Test.ExecutionScope.(Value) -> Unit,
) {
    test(name, displayName, testConfig) { value ->
        val graph =
            createGraphFactory<TestAppGraph.Factory>().create(testScope = testScope.backgroundScope)
        context(graph) { fixtureScopedAction(value) }
    }
}

/** This is easier than calling `contextOf<TestAppGraph>` */
context(graph: TestAppGraph)
fun graph() = graph
