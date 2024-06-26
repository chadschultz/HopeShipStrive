package com.example.hopeshipstrive.model

import com.example.hopeshipstrive.network.RetrofitClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import java.time.Clock
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val retrofit: RetrofitClient,
    private val dataSource: TripLocalDataSource,
    private val clock: Clock,
    private val ioDispatcher: CoroutineDispatcher,
) {

    private var timeOfLastRefresh = 0L

    private val _trips =
        MutableStateFlow<Result<List<Trip>>>(Result.success(emptyList()))
    val trips: SharedFlow<Result<List<Trip>>>
        get() = _trips.onSubscription {
            // Whenever there's a new subscriber (typically ViewModels), see if we need fresh data.
            fetchTripsIfNecessary()
        }

    /**
     * Refresh the data from the server. Useful when user manually requests a refresh.
     */
//    suspend fun forceRefresh() {
//        fetchTrips()
//    }

    /**
     * Load data from the server if we have no cached data, or if the cached data is expired.
     */
    private suspend fun fetchTripsIfNecessary() {
        val noCachedData = _trips.value.getOrNull().isNullOrEmpty()
        if (noCachedData) {
            // Launch separately from fetchTrips so we don't wait for the server response
            // and so they separately update _trips.value
            CoroutineScope(ioDispatcher).launch {
                readLocalTrips()
            }
        }
        val cachedDataExpired = timeOfLastRefresh + TTL_MILLIS < currentMillis()
        // Also true if we have never fetched from the server
        if (cachedDataExpired) {
            CoroutineScope(ioDispatcher).launch {
                fetchTrips()
            }
        }
    }

    private suspend fun readLocalTrips() {
        _trips.value = try {
            val trips = dataSource.getTrips()
            tripsToResult(trips.trips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchTrips() {
        _trips.value = try {
            val tripResponse = retrofit.tripApi.getTrips()
            timeOfLastRefresh = currentMillis()
            tripsToResult(tripResponse.trips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun tripsToResult(trips: List<Trip>): Result<List<Trip>> {
        return Result.success(correctData(onlyValidItemsIn(trips)))
    }

    /**
     * This is the place to make basic corrections to the data, for cases when the data from the
     * server is not ideal. For example, the waypoints were not listed in the order they
     * are used in the trip.
     * Transformations meant just for a specific part of the UI are best done elsewhere. Like how
     * TripListItemTransformer converts the data for use in the RecyclerView in MainActivity.
     */
    private fun correctData(trips: List<Trip>): List<Trip> {
        return trips.map { trip ->
            val sortedLegs = trip.plannedRoute.legs.sortedBy { it.position }
            val sortedWaypointIds = listOfNotNull(sortedLegs.firstOrNull()?.startWaypointId) +
                        sortedLegs.map { it.endWaypointId }
            val waypointMap = trip.waypoints.associateBy { it.id }
            val sortedWaypoints = sortedWaypointIds.mapNotNull { waypointMap[it] }
            trip.copy(waypoints = sortedWaypoints)
        }
    }

    private fun onlyValidItemsIn(items: List<Trip>): List<Trip> {
        // This is where we would filter out invalid items. Not all repositories may need this.
        // However, without detailed documentation on the API, it's unclear which items are invalid.
        // I suspect there are specific rules around which Waypoints Passengers will be listed in,
        // and that the trip anchor type affects those rules. I think some of the trips may not be
        // valid, but it's hard to be sure.
        //
        // There are a number of other possible quality checks (comparing passengers in the Trip to
        // Passengers across all the Trip's Waypoints, for example, or verifying each Leg's
        // endWaypointId matches the next one's startWaypointId). One example check follows.
        return items.filter {
            // Legs represent the spaces between waypoints, so there will always be one more
            // waypoint than leg.
            it.waypoints.size == it.plannedRoute.legs.size + 1
        }
    }

    private fun currentMillis() = clock.millis()

    companion object {
        private val TTL_MILLIS = TimeUnit.HOURS.toMillis(1)
    }
}