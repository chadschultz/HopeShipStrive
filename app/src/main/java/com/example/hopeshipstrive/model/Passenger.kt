package com.example.hopeshipstrive.model

import com.google.gson.annotations.SerializedName

data class Passenger(
    val uuid: String? = null,
    @SerializedName("booster_seat") val boosterSeat: Boolean
)