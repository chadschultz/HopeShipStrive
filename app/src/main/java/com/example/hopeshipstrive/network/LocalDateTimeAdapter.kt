package com.example.hopeshipstrive.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Note: a more sophisticated solution would be to have times displayed in the time zone for each
 * individual Waypoint, to cover the uncommon case where the trip will cross a time zone boundary.
 * That would require backend changes.
 */
class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        value?.let { localDateTime ->
            out.value(
                localDateTime.atZone(ZoneId.systemDefault()).toInstant().toString()
            )
        } ?: out.nullValue()
    }

    override fun read(`in`: JsonReader): LocalDateTime? {
        return if (`in`.peek() == JsonToken.NULL) {
            `in`.nextNull()
            null
        } else {
            LocalDateTime.ofInstant(
                Instant.parse(`in`.nextString()), ZoneId.systemDefault()
            )
        }
    }
}