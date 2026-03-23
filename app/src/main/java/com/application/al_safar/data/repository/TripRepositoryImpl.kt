package com.application.al_safar.data.repository

import com.application.al_safar.data.local.dao.TripDao
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.mapper.toDto
import com.application.al_safar.data.mapper.toEntity
import com.application.al_safar.data.remote.api.TripApi
import com.application.al_safar.domain.model.Trip
import com.application.al_safar.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TripRepositoryImpl(
    private val tripApi: TripApi,
    private val tripDao: TripDao,
    private val preferencesManager: PreferencesManager
) : TripRepository {

    override fun getTrips(): Flow<List<Trip>> {
        syncTripsInBackground()
        return tripDao.getTripsByUserId(getCurrentUserId()).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTripById(tripId: String): Result<Trip> = runCatching {
        val response = tripApi.getTripById(tripId)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.toDomain()
        } else {
            throw Exception("Failed to fetch trip: ${response.message()}")
        }
    }

    override suspend fun createTrip(trip: Trip): Result<Trip> = runCatching {
        val response = tripApi.createTrip(trip.toDto())
        if (response.isSuccessful && response.body() != null) {
            val created = response.body()!!.toDomain(getCurrentUserId())
            tripDao.insertTrip(created.toEntity())
            created
        } else {
            throw Exception("Failed to create trip: ${response.message()}")
        }
    }

    override suspend fun updateTrip(trip: Trip): Result<Trip> = runCatching {
        val response = tripApi.updateTrip(trip.id, trip.toDto())
        if (response.isSuccessful && response.body() != null) {
            val updated = response.body()!!.toDomain(trip.userId)
            tripDao.updateTrip(updated.toEntity())
            updated
        } else {
            throw Exception("Failed to update trip: ${response.message()}")
        }
    }

    override suspend fun deleteTrip(tripId: String): Result<Unit> = runCatching {
        val response = tripApi.deleteTrip(tripId)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete trip: ${response.message()}")
        }
    }

    override suspend fun syncTrips() {
        try {
            val userId = getCurrentUserId()
            if (userId.isEmpty()) return
            val response = tripApi.getTrips()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.forEach { dto ->
                    tripDao.insertTrip(dto.toDomain(userId).toEntity())
                }
            }
        } catch (e: Exception) {
            // Use cached data on failure
        }
    }

    private fun syncTripsInBackground() {
        // Fire-and-forget sync
    }

    private fun getCurrentUserId(): String = runCatching {
        kotlinx.coroutines.runBlocking { preferencesManager.userId.first() ?: "" }
    }.getOrDefault("")
}
