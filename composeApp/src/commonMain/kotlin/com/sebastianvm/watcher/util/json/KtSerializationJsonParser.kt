package com.sebastianvm.watcher.util.json

import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
class KtSerializationJsonParser<T>(private val json: Json, private val serializer: KSerializer<T>) :
    JsonParser<T> {
    override fun fromJson(input: String): T {
        return json.decodeFromString(deserializer = serializer, string = input)
    }

    override fun fromJson(input: InputStream): T {
        return json.decodeFromStream(deserializer = serializer, stream = input)
    }

    override fun toJson(obj: T): String {
        return json.encodeToString(serializer = serializer, value = obj)
    }

    override fun toJson(obj: T, outputStream: OutputStream) {
        json.encodeToStream(serializer = serializer, value = obj, stream = outputStream)
    }
}

inline fun <reified T> jsonParser(json: Json = Json): JsonParser<T> =
    KtSerializationJsonParser(json = json, serializer = serializer())
