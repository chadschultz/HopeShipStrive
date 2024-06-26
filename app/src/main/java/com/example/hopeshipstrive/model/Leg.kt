package com.example.hopeshipstrive.model

import com.google.gson.annotations.SerializedName

data class Leg(
    val position: Int,
    @SerializedName("start_waypoint_id") val startWaypointId: Int,
    @SerializedName("end_waypoint_id") val endWaypointId: Int
)