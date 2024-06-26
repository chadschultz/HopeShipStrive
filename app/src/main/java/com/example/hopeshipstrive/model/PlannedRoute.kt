package com.example.hopeshipstrive.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PlannedRoute(
    @SerializedName("total_time") val totalTime: Double,
    @SerializedName("total_distance") val totalDistance: Int,
    @SerializedName("starts_at") val startsAt: LocalDateTime,
    @SerializedName("ends_at") val endsAt: LocalDateTime,
    val legs: List<Leg>
)