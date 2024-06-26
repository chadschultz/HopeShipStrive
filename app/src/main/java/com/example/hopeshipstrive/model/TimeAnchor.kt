package com.example.hopeshipstrive.model

import com.google.gson.annotations.SerializedName

enum class TimeAnchor {
    @SerializedName("pick_up") PICK_UP,
    @SerializedName("drop_off") DROP_OFF,
}