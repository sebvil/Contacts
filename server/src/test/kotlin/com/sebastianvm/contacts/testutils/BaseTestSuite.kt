package com.sebastianvm.contacts.testutils

import com.sebastianvm.contacts.db.createDatabase
import de.infix.testBalloon.framework.core.Test
import de.infix.testBalloon.framework.core.TestCompartment
import de.infix.testBalloon.framework.core.TestFixture
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import kotlin.uuid.Uuid

@TestRegistering
internal fun baseDbTestSuite(
    @TestElementName name: String = "",
    content:
        TestFixture.Scope<
            suspend TestDependencyContainer.(testExecutionScope: Test.ExecutionScope) -> Unit
        >.() -> Unit,
): Lazy<TestSuite> =
    testSuite(name, compartment = { TestCompartment.Sequential }) {
        testFixture {
            val db = createDatabase(DB_URL, developmentMode = true)
            TestDependencyContainer(db)
        } closeWith
            {
                db?.connector()?.executeInBatch(listOf("SHUTDOWN"))
            } asContextForEach
            {
                content()
            }
    }

@TestRegistering
internal fun baseTestSuite(
    @TestElementName name: String = "",
    content:
        TestFixture.Scope<
            suspend TestDependencyContainer.(testExecutionScope: Test.ExecutionScope) -> Unit
        >.() -> Unit,
): Lazy<TestSuite> =
    testSuite(name) { testFixture { TestDependencyContainer(null) } asContextForEach { content() } }

@TestRegistering
internal fun TestFixture.Scope<
    suspend TestDependencyContainer.(testExecutionScope: Test.ExecutionScope) -> Unit
>
    .testWithUser(
    name: String,
    action: suspend TestDependencyContainer.(Test.ExecutionScope, userId: Uuid) -> Unit,
) =
    test(name) { scope ->
        val userId =
            userRepository.createUser(TestConstants.USERNAME, TestConstants.VALID_PASSWORD)!!
        action(scope, userId)
    }

private const val DB_URL = "r2dbc:h2:mem:///test;DB_CLOSE_DELAY=-1"
