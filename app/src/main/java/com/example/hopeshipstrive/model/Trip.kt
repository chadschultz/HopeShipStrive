package com.example.hopeshipstrive.model

import com.google.gson.annotations.SerializedName

data class Trip(
    @SerializedName("estimated_earnings") val estimatedEarnings: Int,
    val slug: String,
    @SerializedName("time_anchor") val timeAnchor: TimeAnchor,
    @SerializedName("in_series") val inSeries: Boolean? = null,
    val passengers: List<Passenger>,
    @SerializedName("planned_route") val plannedRoute: PlannedRoute,
    val waypoints: List<Waypoint>,
)