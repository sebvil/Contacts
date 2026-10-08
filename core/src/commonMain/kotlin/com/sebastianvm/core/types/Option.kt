package com.sebastianvm.core.types

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Option.Serializer::class)
sealed interface Option<out T> {

    class Serializer<T>(val serializer: KSerializer<T>) : KSerializer<Option<T>> {
        override val descriptor: SerialDescriptor =
            SerialDescriptor("Option", serializer.descriptor)

        @OptIn(ExperimentalSerializationApi::class)
        override fun serialize(
            encoder: Encoder,
            value: Option<T>,
        ) {
            value.ifExists {
                encoder.encodeNullableSerializableValue(serializer = serializer, value = it)
            }
        }

        override fun deserialize(decoder: Decoder): Option<T> {
            val res = decoder.decodeSerializableValue(serializer)
            return Some(res)
        }
    }
}

@Serializable data object None : Option<Nothing>

@Serializable data class Some<T>(val value: T) : Option<T>

inline fun <T> Option<T>.ifExists(action: (T) -> Unit) {
    (this as? Some)?.let { action(it.value) }
}

inline fun <T, U> Option<T>.map(transform: (T) -> U): Option<U> {
    return when (this) {
        is Some<T> -> Some(transform(this.value))
        is None -> None
    }
}

fun <T> Option<T>.getOrElse(default: () -> T): T {
    return when (this) {
        is Some<T> -> value
        is None -> default()
    }
}

fun <T> Option<T>.getOrThrow(): T {
    return when (this) {
        is Some<T> -> value
        is None -> throw NoSuchElementException()
    }
}
