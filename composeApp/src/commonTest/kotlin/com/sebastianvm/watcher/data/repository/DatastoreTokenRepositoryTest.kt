package com.sebastianvm.watcher.data.repository

import app.cash.turbine.test
import com.sebastianvm.watcher.di.TestAppGraph
import com.sebastianvm.watcher.di.graph
import com.sebastianvm.watcher.di.testWithDependencies
import com.sebastianvm.watcher.model.AccessToken
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.flow.first

val DatastoreTokenRepositoryTest by testSuite {
    context(graph: TestAppGraph)
    fun makeSubject() = DatastoreTokenRepository(graph.tokenDataStore)

    testWithDependencies("saveToken updates datastore") {
        val subject = makeSubject()
        subject.saveToken(ACCESS_TOKEN)
        graph().tokenDataStore.data.first() shouldBe ACCESS_TOKEN
        subject.saveToken(SECOND_ACCESS_TOKEN)
        graph().tokenDataStore.data.first() shouldBe SECOND_ACCESS_TOKEN
    }

    testWithDependencies("getToken returns saved token") {
        val subject = makeSubject()
        subject.getToken().test {
            awaitItem().shouldBeNull()
            graph().tokenDataStore.updateData { ACCESS_TOKEN }
            awaitItem() shouldBe ACCESS_TOKEN
        }
    }

    testWithDependencies("clearToken removes token from datastore") {
        graph().tokenDataStore.updateData { ACCESS_TOKEN }

        val subject = makeSubject()
        subject.clearToken()
        graph().tokenDataStore.data.first().shouldBeNull()
    }
}

private val ACCESS_TOKEN = AccessToken("token", 1.minutes, "refresh-token")
private val SECOND_ACCESS_TOKEN = AccessToken("second-token", 2.minutes, "second-refresh-token")
