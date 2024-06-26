package com.example.hopeshipstrive.model

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TripLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    suspend fun getTrips(): TripResponse = withContext(Dispatchers.IO) {
        val assetManager = context.assets

        val tripResponse = assetManager.open("Trip.json").reader().use { reader ->
            gson.fromJson(reader, TripResponse::class.java)
        }

        tripResponse
    }
}