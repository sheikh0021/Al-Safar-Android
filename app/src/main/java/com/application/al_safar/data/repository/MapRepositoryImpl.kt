package com.application.al_safar.data.repository

import com.application.al_safar.data.local.dao.PlaceDao
import com.application.al_safar.data.mapper.toDomain
import com.application.al_safar.data.mapper.toEntity
import com.application.al_safar.data.remote.api.MapApi
import com.application.al_safar.domain.model.Place
import com.application.al_safar.domain.repository.MapRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MapRepositoryImpl(
    private val mapApi: MapApi,
    private val placeDao: PlaceDao
) : MapRepository {

    override fun getPlaces(category: String?): Flow<List<Place>> {
        return if (category != null) {
            placeDao.getPlacesByCategory(category).map { it.map { entity -> entity.toDomain() } }
        } else {
            placeDao.getAllPlaces().map { it.map { entity -> entity.toDomain() } }
        }
    }

    override suspend fun getNearbyPlaces(
        latitude: Double,
        longitude: Double,
        radius: Int
    ): Result<List<Place>> = runCatching {
        val response = mapApi.getNearbyPlaces(latitude, longitude, radius)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.map { it.toDomain() }
        } else {
            throw Exception("Failed to fetch nearby places: ${response.message()}")
        }
    }

    override suspend fun toggleFavorite(placeId: String, isFavorite: Boolean) {
        placeDao.updateFavoriteStatus(placeId, isFavorite)
    }

    override suspend fun syncPlaces() {
        try {
            val response = mapApi.getPlaces(null)
            if (response.isSuccessful && response.body() != null) {
                placeDao.insertPlaces(response.body()!!.map { it.toDomain().toEntity() })
            }
        } catch (e: Exception) {
            // Use cached data on failure
        }
    }
}
