
package com.vibecoding.trippilot.domain.repository

import com.vibecoding.trippilot.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    fun getTrips(): Flow<List<Trip>>

    suspend fun getTripById(id: Int): Trip?

    suspend fun insertTrip(trip: Trip)

    suspend fun deleteTrip(trip: Trip)

    suspend fun generateItinerary(trip: Trip): String
}
