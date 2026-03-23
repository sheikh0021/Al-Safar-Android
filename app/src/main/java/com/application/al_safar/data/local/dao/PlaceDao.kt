package com.application.al_safar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.application.al_safar.data.local.entities.PlaceEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaceDao {
    @Query("SELECT * FROM places ORDER BY name ASC")
    fun getAllPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE id = :placeId")
    fun getPlaceById(placeId: String): Flow<PlaceEntity?>

    @Query("SELECT * FROM places WHERE category = :category ORDER BY name ASC")
    fun getPlacesByCategory(category: String): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoritePlaces(): Flow<List<PlaceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    @Update
    suspend fun updatePlace(place: PlaceEntity)

    @Query("UPDATE places SET isFavorite = :isFavorite WHERE id = :placeId")
    suspend fun updateFavoriteStatus(placeId: String, isFavorite: Boolean)

    @Query("DELETE FROM places")
    suspend fun clearAll()
}