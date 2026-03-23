package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTrips(): Flow<List<Trip>>
    suspend fun getTripById(tripId: String): Result<Trip>
    suspend fun createTrip(trip: Trip): Result<Trip>
    suspend fun updateTrip(trip: Trip): Result<Trip>
    suspend fun deleteTrip(tripId: String): Result<Unit>
    suspend fun syncTrips()
}
