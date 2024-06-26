package com.example.hopeshipstrive.recyclerview

sealed class TripListItem {
    data class HeaderItem(
        val formattedDate: String,
        val formattedStartTime: String,
        val formattedEndTime: String,
        val formattedEarnings: String,
    ) : TripListItem()

    data class TripItem(
        val formattedPickupTime: String,
        val formattedDropoffTime: String,
        val numRiders: Int,
        val numBoosters: Int,
        val formattedEarnings: String,
        val formattedWaypoints: String,
    ) : TripListItem()
}