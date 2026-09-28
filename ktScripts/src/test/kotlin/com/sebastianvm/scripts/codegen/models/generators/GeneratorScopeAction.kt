package com.sebastianvm.scripts.codegen.models.generators

import com.sebastianvm.scripts.util.EchoHandler
import de.infix.testBalloon.framework.core.Test

typealias GeneratorScopeAction =
    suspend EchoHandler.(testExecutionScope: Test.ExecutionScope) -> Unit
