package com.sebastianvm.contacts.testUtils

import kotlin.uuid.Uuid
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun json(vararg keys: Pair<String, JsonElement>): JsonObject {
    return JsonObject(mapOf(*keys))
}

infix fun String.to(element: String) = this to JsonPrimitive(element)

infix fun String.to(element: Uuid) = this to JsonPrimitive(element.toString())
