package com.example.hopeshipstrive.network

import com.example.hopeshipstrive.model.TripResponse
import retrofit2.http.GET

interface TripApi {
    @GET("Trip.json")
    suspend fun getTrips(): TripResponse
}