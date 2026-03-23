package com.application.al_safar.domain.repository

import com.application.al_safar.domain.model.Place
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun getPlaces(category: String?): Flow<List<Place>>
    suspend fun getNearbyPlaces(latitude: Double, longitude: Double, radius: Int): Result<List<Place>>
    suspend fun toggleFavorite(placeId: String, isFavorite: Boolean)
    suspend fun syncPlaces()
}
