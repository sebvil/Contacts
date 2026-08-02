package com.sebastianvm.contacts.testUtils

import com.sebastianvm.contacts.di.TestAppGraph
import com.sebastianvm.contacts.module
import de.infix.testBalloon.framework.core.Test
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.TestFixture
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import de.infix.testBalloon.framework.shared.TestSuitePropertyName
import dev.zacsweers.metro.createGraph
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication

@TestRegistering
fun ktorTestSuite(
    @TestElementName name: String? = null,
    testConfig: TestConfig = TestConfig,
    @TestSuitePropertyName qualifiedPropertyName: String = "",
    content: TestFixture.Scope<suspend Test.ExecutionScope.(TestAppGraph) -> Unit>.() -> Unit,
) =
    testSuite(name, testConfig, qualifiedPropertyName) {
        testFixture {
            createGraph<TestAppGraph>()
        } closeWith
            {
                postgres.close()
            } asParameterForAll
            {
                content()
            }
    }

@TestRegistering
fun KtorTestSuite.applicationTest(
    @TestElementName name: String,
    testConfig: TestConfig = TestConfig,
    action: suspend TestDependencies.() -> Unit,
) =
    test(name, testConfig) { appGraph ->
        testApplication {
            application {
                module(appGraph.routes())
            }
            client = createClient {
                install(ContentNegotiation) {
                    json()
                }
                install(Resources)
                defaultRequest {
                    contentType(ContentType.Application.Json)
                }
            }

            val testDependencies =
                TestDependencies(
                    appGraph = appGraph,
                    client = client,
                    testExecutionScope = this@test,
                )
            testDependencies.action()
        }
    }

@TestRegistering
fun KtorTestSuite.contractTest(
    testConfig: TestConfig = TestConfig,
    executeRequest: suspend HttpClient.() -> HttpResponse,
    expectedStatus: HttpStatusCode,
) =
    applicationTest(name = "contract is valid", testConfig = testConfig) {
        val response = client.executeRequest()
        response.status shouldBe expectedStatus
    }

typealias KtorTestSuite = TestFixture.Scope<suspend Test.ExecutionScope.(TestAppGraph) -> Unit>
