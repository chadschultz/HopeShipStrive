package com.example.hopeshipstrive.recyclerview

import android.icu.text.NumberFormat
import com.example.hopeshipstrive.datastate.DataStateTransformer
import com.example.hopeshipstrive.model.Trip
import com.example.hopeshipstrive.model.Waypoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TripListItemTransformer : DataStateTransformer<List<TripListItem>, List<Trip>>() {
    private val dateFormatter = DateTimeFormatter.ofPattern("EEE M/dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm")
    private val currencyFormatter = NumberFormat.getCurrencyInstance()

    override fun transformData(sourceData: List<Trip>): List<TripListItem> {
        val tripListItems = mutableListOf<TripListItem>()
        sourceData.groupBy {
            dateFormatter.format(it.plannedRoute.startsAt)
        }.forEach { (dateString, trips) ->
            tripListItems.add(headerItemFrom(dateString, trips))
            tripListItems.addAll(trips.map { trip -> tripItemFrom(trip) })
        }
        return tripListItems
    }

    private fun headerItemFrom(dateString: String, trips: List<Trip>): TripListItem.HeaderItem {
        val earliestTime = trips.minOf { it.plannedRoute.startsAt }
        val latestTime = trips.maxOf { it.plannedRoute.endsAt }
        val totalEarnings = trips.sumOf { it.estimatedEarnings }
        return TripListItem.HeaderItem(
            dateString,
            formatTime(earliestTime),
            formatTime(latestTime),
            formatEstimatedEarnings(totalEarnings),
        )
    }

    private fun tripItemFrom(trip: Trip): TripListItem.TripItem {
        return TripListItem.TripItem(
            formattedPickupTime = formatTime(trip.plannedRoute.startsAt),
            formattedDropoffTime = formatTime(trip.plannedRoute.endsAt),
            numRiders = trip.passengers.size,
            numBoosters = trip.passengers.count { it.boosterSeat },
            formattedEarnings = formatEstimatedEarnings(trip.estimatedEarnings),
            formattedWaypoints = formatWaypoints(trip.waypoints),
        )
    }

    private fun formatTime(time: LocalDateTime): String {
        // DateTimeFormatter and SimpleDateFormat only use "AM" and "PM". Hack
        // to provide the required "a" or "p" instead.
        val aOrP = if (time.hour < 12) "a" else "p"
        return timeFormatter.format(time) + aOrP
    }

    private fun formatEstimatedEarnings(pennies: Int): String {
        val dollars = pennies / 100.0
        return currencyFormatter.format(dollars)
    }

    private fun formatWaypoints(waypoints: List<Waypoint>): String {
        return waypoints.mapIndexed { index, waypoint ->
            "${index + 1}. ${waypoint.location.address}"
        }.joinToString("\n")
    }

}