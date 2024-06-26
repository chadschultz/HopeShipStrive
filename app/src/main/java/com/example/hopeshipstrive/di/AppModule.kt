package com.example.hopeshipstrive.di

import com.example.hopeshipstrive.model.TimeAnchor
import com.example.hopeshipstrive.network.LocalDateTimeAdapter
import com.example.hopeshipstrive.network.TimeAnchorAdapter
import com.example.hopeshipstrive.recyclerview.TripListItemTransformer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.Clock
import java.time.LocalDateTime

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(TimeAnchor::class.java, TimeAnchorAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun providesTripListItemTransformer(): TripListItemTransformer {
        return TripListItemTransformer()
    }

}