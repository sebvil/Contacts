package com.sebastianvm.core.types

import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val OptionSerializationTest by testSuite {
    listOf(
            "{}" to None,
            """{ "value": "hello" }""" to Some("hello"),
            """{ "value": null }""" to Some(null),
        )
        .forEach { (jsonString, jsonClass) ->
            test("parsing $jsonString returns $jsonClass") {
                val res = Json.decodeFromString<TestDataClass>(jsonString)
                res shouldBe TestDataClass(jsonClass)
            }

            test("serializing $jsonClass returns $jsonString") {
                val res = Json.encodeToString<TestDataClass>(TestDataClass(jsonClass))
                res shouldBe jsonString.replace(" ", "")
            }
        }

    listOf(
            "{}" to OuterClass(),
            """{ "value": { "value": 1 } }""" to OuterClass(Some(InnerClass(value = 1))),
        )
        .forEach { (jsonString, jsonClass) ->
            test("parsing $jsonString returns $jsonClass") {
                val res = Json.decodeFromString<OuterClass>(jsonString)
                res shouldBe jsonClass
            }

            test("serializing $jsonClass returns $jsonString") {
                val res = Json.encodeToString<OuterClass>(jsonClass)
                res shouldBe jsonString.replace(" ", "")
            }
        }
}

@Serializable private data class TestDataClass(val value: Option<String?> = None)

@Serializable private data class OuterClass(val value: Option<InnerClass> = None)

@Serializable data class InnerClass(val value: Int)
