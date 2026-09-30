package com.sebastianvm.scripts.codegen.models.generators

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.sebastianvm.scripts.codegen.models.GenerateModels
import com.sebastianvm.scripts.codegen.models.defintions.processed.ProcessedModelDefinition
import com.sebastianvm.scripts.codegen.models.defintions.yaml.ModelDefinition
import com.sebastianvm.scripts.codegen.models.util.poet.getProcessedFileContents
import com.sebastianvm.scripts.util.EchoHandler
import de.infix.testBalloon.framework.core.Test
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.TestFixture.Scope
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestElementName
import de.infix.testBalloon.framework.shared.TestRegistering
import de.infix.testBalloon.framework.shared.TestSuitePropertyName
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import org.intellij.lang.annotations.Language

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

context(_: EchoHandler)
fun getProcessedModels(): List<ProcessedModelDefinition> {
    val models =
        listOf(Fixtures.BASE_MODEL_YAML, Fixtures.RELATED_MODEL).map {
            Yaml(configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property))
                .decodeFromString<ModelDefinition>(it)
        }
    return GenerateModels.processModels(models)
}

infix fun Generator.generatedOutputShouldBe(@Language("kt") expected: String) {
    generate().getProcessedFileContents() shouldBe expected
}
