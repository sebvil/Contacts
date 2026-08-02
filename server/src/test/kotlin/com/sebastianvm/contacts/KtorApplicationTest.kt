package com.sebastianvm.contacts

import de.infix.testBalloon.framework.core.Test
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.TestSuiteScope
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication

@TestRegistering
fun TestSuiteScope.applicationTest(
    @TestElementName name: String,
    testConfig: TestConfig = TestConfig,
    action: suspend context(ApplicationTestBuilder) Test.ExecutionScope.() -> Unit,
) =
    test(name, testConfig) {
        testApplication {
            application {
                module()
            }
            client = createClient {
                install(ContentNegotiation) {
                    json()
                }
                install(Resources)
            }
            action()
        }
    }

@TestRegistering
fun TestSuiteScope.contractTest(
    testConfig: TestConfig = TestConfig,
    executeRequest: suspend HttpClient.() -> HttpResponse,
    expectedStatus: HttpStatusCode,
) =
    applicationTest("contract is valid", testConfig) {
        val response = client.executeRequest()
        response.status shouldBe expectedStatus
    }

context(applicationTestBuilder: ApplicationTestBuilder)
val client: HttpClient
    get() = applicationTestBuilder.client
