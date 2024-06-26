package com.example.hopeshipstrive.model

data class Waypoint(
    val id: Int,
    val location: Location,
    val passengers: List<Passenger>
)