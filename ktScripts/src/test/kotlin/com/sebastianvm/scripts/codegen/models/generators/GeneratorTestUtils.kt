package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.util.EchoHandler
import de.infix.testBalloon.framework.core.Test
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.TestFixture.Scope
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import de.infix.testBalloon.framework.shared.TestSuitePropertyName

@TestRegistering
fun generatorTestSuite(
    @TestElementName name: String? = null,
    testConfig: TestConfig = TestConfig,
    @TestSuitePropertyName qualifiedPropertyName: String = "",
    content:
        Scope<suspend EchoHandler.(testExecutionScope: Test.ExecutionScope) -> Unit>.() -> Unit,
) =
    testSuite(name, testConfig, qualifiedPropertyName) {
        testFixture {
            val handler: EchoHandler = object : EchoHandler {}
            handler
        } asContextForEach (content)
    }
