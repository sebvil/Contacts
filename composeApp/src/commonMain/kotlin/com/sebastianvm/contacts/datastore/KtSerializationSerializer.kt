package com.sebastianvm.contacts.datastore

import androidx.datastore.core.Serializer
import com.sebastianvm.contacts.util.json.JsonParser
import java.io.InputStream
import java.io.OutputStream

class KtSerializationSerializer<T>(
    private val jsonParser: JsonParser<T>,
    override val defaultValue: T,
) : Serializer<T> {

    override suspend fun readFrom(input: InputStream): T {
        return jsonParser.fromJson(input)
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        jsonParser.toJson(obj = t, outputStream = output)
    }
}
