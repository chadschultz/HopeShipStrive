package com.example.hopeshipstrive.network

import com.example.hopeshipstrive.model.TimeAnchor
import com.google.gson.TypeAdapter

class TimeAnchorAdapter : TypeAdapter<TimeAnchor>() {

    override fun write(out: com.google.gson.stream.JsonWriter, value: TimeAnchor?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.name.lowercase())
        }
    }

    override fun read(`in`: com.google.gson.stream.JsonReader): TimeAnchor? {
        return when (`in`.nextString()) {
            "pick_up" -> TimeAnchor.PICK_UP
            "drop_off" -> TimeAnchor.DROP_OFF
            else -> null
        }
    }
}