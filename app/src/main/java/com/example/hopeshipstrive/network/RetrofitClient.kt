package com.example.hopeshipstrive.network

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor(gson: Gson) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://hopskipdrive-static-files.s3.us-east-2.amazonaws.com/interview-resources/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val tripApi: TripApi = retrofit.create(TripApi::class.java)
}