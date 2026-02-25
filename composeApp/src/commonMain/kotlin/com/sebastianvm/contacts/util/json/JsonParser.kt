package com.sebastianvm.contacts.util.json

import java.io.InputStream
import java.io.OutputStream

/**
 * Represents a generic parser to handle JSON serialization and deserialization.
 *
 * @param T The type of object that this parser operates on.
 */
interface JsonParser<T> {
    /**
     * Parses the given JSON string and converts it into an instance of type T.
     *
     * @param input The JSON string to be parsed.
     * @return An object of type T represented by the JSON string.
     */
    fun fromJson(input: String): T

    /**
     * Parses the given InputStream containing JSON data and converts it into an instance of type T.
     *
     * @param input The InputStream containing JSON content to be parsed.
     * @return An object of type T constructed from the JSON data.
     */
    fun fromJson(input: InputStream): T

    /**
     * Converts the given object of type T into its JSON string representation.
     *
     * @param obj The object of type T to be serialized into JSON.
     * @return A JSON string representation of the provided object.
     */
    fun toJson(obj: T): String

    /**
     * Serializes the given object of type T into its JSON representation and writes it to the
     * specified OutputStream.
     *
     * @param obj The object of type T to serialize into JSON.
     * @param outputStream The output stream to which the JSON representation of the object is
     *   written.
     */
    fun toJson(obj: T, outputStream: OutputStream)
}
